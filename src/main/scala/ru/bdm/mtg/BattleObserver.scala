package ru.bdm.mtg

import org.h2.Driver
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}
import ru.bdm.mtg.cards.{HandOfEmrakul, UlamogsCrusher}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, ObjectOutputStream, OutputStream, PrintStream}
import java.net.{ServerSocket, Socket}
import java.sql.DriverManager
import java.util.Scanner
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.io.StdIn.readLine


class ConsolePlayerBattle() {
  var state = BattleObserver.startState(DeckShuffler.allCard.getSeq)

  println("start battle")
  while (!BattleObserver.isEndStates(state)) {
    println(state)
    val next = BattleObserver.nextStates(state)
    if (next.size > 1) {
      println("choose next state")
      next.zipWithIndex.foreach { case (state, index) =>
        println(s"$index: $state")
      }
      print("choose number:")
      state = next(StdIn.readInt())
    }
    if (next.size == 1) {
      println(state.getChanges(next.head))
      state = next.head
    }
  }

}

object Main {
  def main(args: Array[String]): Unit = {
    new NP().start()
  }

}

class SocketManager extends Thread{
  val server = new ServerSocket(ClientSock.port)
  implicit val executor = ExecutionContext.global

  val clients = mutable.Map[Socket, PrintStream]()
  setDaemon(true)

  override def run(): Unit = {
    while (!Thread.interrupted()) {
      try {
        val client = server.accept()
        clients(client) = new PrintStream(client.getOutputStream)
      } catch {
        case e: Throwable =>
          println(e.getMessage)
      }
    }
  }

  def print(s:String): Unit = {
    Future {
      var removed = List.empty[Socket]
      for ((key, printer) <- clients) {
        try {
          if (key.isClosed)
            removed ::= key
          else
            printer.println(s)
        } catch {
          case e: Throwable =>
            key.close()
            removed ::= key
        }
      }
    }
  }
}
object ClientSock{
  val port = 23001

  def main(arg: Array[String]): Unit = {

    print("enter address:")
    val sock = new Socket(readLine(), port)
    val sc = new Scanner(sock.getInputStream)
    while (true)
      println(sc.nextLine())
  }
}

class NP {
  var state = BattleObserver.startState(DeckShuffler.allCard.getSeq)
  implicit val executor = ExecutionContext.global
  val sock = new SocketManager()
  sock.start()


  var num_rec = 0
  val MB = 1024 * 1024.0
  var endsCount = 0L
  var endSuccess = 0L

  DriverManager.registerDriver(new Driver)
  val conn = DriverManager.getConnection("jdbc:h2:~/mtg-db", "bdm", "1234")

  BattleObserver.nextStates(state).foreach{i => println(i.score)}


  def addInDatabase(state: State, value: Int): Unit = {
    Future {
      val sql = "insert into mtg (state, value) values (?, ?);"
      val st = conn.prepareStatement(sql)
      st.setBinaryStream(1, getStream(state))
      st.setInt(2, value)
      st.executeUpdate()
    }
  }

  def getStream(state: State): InputStream = {
    val stream = new ByteArrayOutputStream()
    val objStream = new ObjectOutputStream(stream)
    objStream.writeObject(state)
    objStream.flush()
    objStream.close()
    new ByteArrayInputStream(stream.toByteArray)
  }

  def memory = {
    Runtime.getRuntime.totalMemory() / MB
  }

  def maxMemory = {
    Runtime.getRuntime.maxMemory() / MB
  }

  var iter = 0
  val iterPrint = 5000
  val start_time = System.currentTimeMillis()

  def getTime(pr: Double): Double = {
    (System.currentTimeMillis() - start_time).toDouble / (10 * 60 * 60) / pr
  }

  def start(): Unit = {
    f(state, 0.0, 100)
  }

  def f(state: State, st_p: Double, len_p: Double): Int = {
    onPrint(st_p)

    if (BattleObserver.isEndStates(state)) {
      val res = if (BattleObserver.containsWinCard(state)) {
        endSuccess += 1
        addInDatabase(state, state.numberTurn)
        state.numberTurn
      } else {
        Int.MaxValue
      }
      endPrint(st_p, res)
      return res
    }
    num_rec += 1
    val next = BattleObserver.nextStates(state)
    val len = len_p / next.size
    val res = next.sortBy(-_.score).zipWithIndex.map { case (s, i) =>
      f(s, st_p + i * len, len)
    }.min
    if (res < Int.MaxValue)
      addInDatabase(state, res)
    num_rec -= 1
    res
  }


  private def onPrint(percent: Double) = {
    iter += 1
    //    if (iter % iterPrint == 0)
    //      println("-> " + num_rec  + " memory=" + memory + " / " + maxMemory + "   " + percent + "% time=" + getTime(percent))
  }

  var min_num = Int.MaxValue

  private def endPrint(percent: Double, value: Int) = {
    endsCount += 1
    if (min_num > num_rec + 1)
      min_num = num_rec + 1
    if (iter % iterPrint == 0) {
      val s = ("-> " + (num_rec + 1) + "(" + min_num + ") turn=" + (if (value == Int.MaxValue) "fail" else value) + " ends=" + endsCount + " success=" + endSuccess + " memory=" + memory + " / " + maxMemory + "   " + (percent * 1000).toInt / 1000.0 + "% time=" + getTime(percent))
      println(s)
      sock.print(s)
    }
  }
}

object BattleObserver {
  private val shuffler = new DeckShuffler(34)

  def startState(deck: Seq[Card]): State =
    State(library = shuffler.shuffle(deck)).copy(draw = 7)

  def nextStates(state: State): Seq[State] = {
    if (state.phase == Phase.takeFirst && state.draw > 0)
      applyDraws(state)
    else if (state.phase == Phase.discardFirst && state.discard > 0)
      applyDiscards(state)
    else if (state.draw > 0)
      applyDraws(state)
    else if (state.discard > 0)
      applyDiscards(state)
    else if (state.shuffle)
      applyShuffleLibrary(state)
    else
      applyPlay(state)
  }

  val winsCards = Seq(HandOfEmrakul(), HandOfEmrakul(true), UlamogsCrusher(), UlamogsCrusher(true))

  def containsWinCard(state: State): Boolean = {
    for (card <- winsCards)
      if (state.battlefield.contains(card))
        return true
    false
  }

  def isEndStates(state: State): Boolean = {
    state.numberTurn >= 7 || containsWinCard(state)
  }

  private def applyPlay(state: State): Seq[State] = {
    val curr = state.copy(phase = Phase.play)
    val actives = activeActions(curr) :+ NextTurn
    actives.flatMap(_.act(curr))
  }

  private def applyDraws(curr: State): Seq[State] = {
    val takes = curr.copy(phase = Phase.take)

    val actives = activeActions(takes)
    if (actives.isEmpty)
      takeCards(takes) :: Nil
    else
      actives.flatMap(_.act(takes))
  }

  private def applyDiscards(curr: State): Seq[State] = {
    val state = curr.copy(phase = Phase.discard)
    val actives = activeActions(state)

    actives.flatMap(_.act(state))
  }

  private def applyShuffleLibrary(state: State): Seq[State] = {
    state.copy(
      library = shuffler.shuffle(state.library ++ state.topOfLibrary),
      topOfLibrary = Seq.empty[Card],
      shuffle = false
    ) :: Nil
  }


  private def activeActions(curr: State): Seq[Action] = {
    allCards(curr).flatMap(_.description.filter { case (condition, _) =>
      condition.check(curr)
    }).map(_._2).toSeq
  }

  private def allCards(curr: State): Iterable[Card] =
    curr.hand.keys ++ curr.lands.keys ++ curr.graveyard.keys ++ curr.battlefield.keys

  private def takeCards(state: State): State = {
    val lib = state.topOfLibrary ++ state.library
    state.copy(
      hand = state.hand ++~ lib.slice(0, state.draw),
      topOfLibrary = state.topOfLibrary.drop(state.draw),
      library = state.library.drop(Math.max(state.draw - state.topOfLibrary.size, 0)),
      draw = 0)
  }
}