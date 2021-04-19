package ru.bdm.mtg

import org.h2.Driver
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}
import ru.bdm.mtg.cards.{Duress, HandOfEmrakul, LotusPetal, UlamogsCrusher}
import ru.bdm.mtg.lands.Permanent

import java.io._
import java.net.{ServerSocket, Socket}
import java.security.MessageDigest
import java.sql.DriverManager
import java.util.Scanner
import scala.annotation.tailrec
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
    val np = new NP()
    for (n <- 2 to 5) {
      BattleObserver.N = n
      for (i <- 1 to 100) {
        println("start i=" + i)

        np.start()
      }
    }
  }

}

class SocketManager extends Thread {
  def close(): Unit = {
    server.close()
    clients = Map.empty
    interrupt()
  }

  val server = new ServerSocket(ClientSock.port)
  implicit val executor = ExecutionContext.global

  var clients = Map[Socket, PrintStream]()
  setDaemon(true)

  override def run(): Unit = {
    while (!Thread.interrupted()) {
      try {
        val client = server.accept()
        clients += client -> new PrintStream(client.getOutputStream)
      } catch {
        case e: Throwable =>
          println(e.getMessage)
      }
    }
  }

  def print(s: String): Unit = {
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
      clients --= removed
    }
  }
}

object ClientSock {
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

  var startId: Option[Int] = None
  var hashs = mutable.Map[Array[Byte], Int]()
  var MAX_SIZE = 15000
  var min_num = Int.MaxValue


  var iter = 0
  val iterPrint = 2000
  var start_time = System.currentTimeMillis()

  def getHash(state: State): Array[Byte] = {
    MessageDigest.getInstance("MD5").digest(getStream(state))
  }

  def getTime(pr: Double): Double = {
    ((System.currentTimeMillis() - start_time).toDouble / (10 * 60) / pr * 1000).toInt / 1000.0
  }

  var startsCount = 0

  def start(): Unit = {
    startsCount += 1
    state = BattleObserver.startState(DeckShuffler.allCard.getSeq)
    hashs = mutable.Map.empty
    iter = 0
    start_time = System.currentTimeMillis()
    startId = None
    num_rec = 0
    endsCount = 0L
    endSuccess = 0L
    min_num = Int.MaxValue
    f(state, 0.0, 100)
    fullEndDatabase()
  }

  def firstAdd(): Unit = {

    val state_arr = getStream(state)

    val sql1 = "insert into starts (state, len, i) values (?, ?, false);"

    val st = conn.prepareStatement(sql1)
    st.setBinaryStream(1, new ByteArrayInputStream(state_arr))
    st.setInt(2, BattleObserver.N)
    st.executeUpdate()
    val sql2 = "select id from starts where state = ?;"
    val st1 = conn.prepareStatement(sql2)
    st1.setBinaryStream(1, new ByteArrayInputStream(state_arr))
    val set = st1.executeQuery()
    set.next()
    startId = Some(set.getInt("id"))
    println(s"firtst add $startId")
  }


  def fullEndDatabase(): Unit = {
    if (startId.isDefined){
      val sql = "update starts set i = true where id = (?);"
      val st = conn.prepareStatement(sql)
      st.setInt(1, startId.get)
      st.executeUpdate()
    }
  }

  def addInDatabase(state: State, value: Int): Unit = {
    if (startId.isEmpty)
      firstAdd()

    val sql = "insert into mtg (state, value, start) values (?, ?, ?);"
    val st = conn.prepareStatement(sql)

    st.setBinaryStream(1, new ByteArrayInputStream(getHash(state)))
    st.setInt(2, value)
    st.setInt(3, startId.get)
    st.executeUpdate()

  }

  def getStream(state: State): Array[Byte] = {
    val arr = new ByteArrayOutputStream()
    val print = new ObjectOutputStream(arr)
    print.writeObject(state)
    arr.toByteArray
  }

  def memory = {
    Runtime.getRuntime.totalMemory() / MB
  }

  def maxMemory = {
    Runtime.getRuntime.maxMemory() / MB
  }


  def f(state: State, st_p: Double, len_p: Double): Int = {
    if((st_p > 50 || endsCount > 1000000) && startId.isEmpty )
      return Int.MaxValue

    onPrint(st_p)
    val hash = getHash(state)
    if (hashs.contains(hash))
      return hashs(hash)


    if (BattleObserver.isEndStates(state)) {
      val res = if (BattleObserver.containsWinCard(state)) {
        endSuccess += 1
        addInDatabase(state, state.numberTurn)
        state.numberTurn
      } else {
        Int.MaxValue
      }
      endPrint(st_p, res)
      hashs(hash) = res
      if (hashs.size > MAX_SIZE)
        hashs.clear()
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
    hashs(hash) = res
    if (hashs.size > MAX_SIZE)
      hashs.clear()
    res
  }


  private def onPrint(percent: Double) = {
    iter += 1

   // if (iter % iterPrint == 0)
   //   println("-> " + num_rec + " [" + iter + "] memory=" + memory + " / " + maxMemory + "   " + percent + "% time=" + getTime(percent))
  }


  private def endPrint(percent: Double, value: Int) = {
    endsCount += 1
    if (min_num > num_rec + 1)
      min_num = num_rec + 1
    if (endsCount % iterPrint == 0) {
      val s = ("-> " + (num_rec + 1) + "(" + min_num + ") [" + startsCount + "] turn=" + (if (value == Int.MaxValue) "fail" else value) + " ends=" + endsCount + " success=" + endSuccess + " memory=" + memory + " / " + maxMemory + "   " + (percent * 1000).toInt / 1000.0 + "% time=" + getTime(percent) + " min")
      println(s)
      sock.print(s)
    }
  }
}

object BattleObserver {
  var N = 3
  private val shuffler = new DeckShuffler(System.currentTimeMillis())

  def startState(deck: Seq[Card]): State =
    State(library = shuffler.shuffle(deck)).copy(draw = 7)

  @tailrec
  def nextStatesSkipOnes(state: State): Seq[State] = {
    val next = nextStates(state)
    if (next.size > 1)
      next
    else
      nextStatesSkipOnes(next.head)
  }

  def nextStates(state: State): Seq[State] = {
    nextStatesNotScore(state).map { st =>
      st.copy(score = evaluate(state, st))
    }
  }

  def nextStatesNotScore(state: State): Seq[State] = {
    val res = if (state.phase == Phase.takeFirst && state.draw > 0)
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

    if (res.isEmpty) applyPlay(state) else res
  }


  def containsWinCard(state: State): Boolean = {
    countWinCard(state.battlefield.getSeq) > 0
  }

  def isEndStates(state: State): Boolean = {
    state.numberTurn >= N || containsWinCard(state)
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

  def countWinCard(seq: Seq[Card]): Int = seq.count {
    case _: UlamogsCrusher => true
    case _: HandOfEmrakul => true
    case _ => false
  }

  def count[CARD, CL](set: Map[CARD, Int], cls: Class[CL]): Int = set.map { v =>
    if (cls.isInstance(v._1)) {
      v._2
    } else
      0
  }.sum

  def contains[CARD, CL](set: Map[CARD, Int], cls: Class[CL]): Boolean = count[CARD, CL](set, cls) > 0

  def evaluate(stateOld: State, stateCurrent: State): Double = {
    var sum = 0.0
    val mana = stateCurrent.manaPool.getSeq.size - stateOld.manaPool.getSeq.size
    val winCardInGraveyard = countWinCard(stateCurrent.graveyard.getSeq) > 0
    if (mana > 0) {
      if (winCardInGraveyard)
        sum += mana * 0.1
      else
        sum -= mana * 0.1
      if (countAddBattle(classOf[Permanent]) < 0)
        sum -= mana * 0.01
    }

    def countAddBattle[CL](cls: Class[CL]) =
      count(stateCurrent.battlefield, cls) - count(stateOld.battlefield, cls)

    def countAddLands[CL](cls: Class[CL]) =
      count(stateCurrent.lands, cls) - count(stateOld.lands, cls)

    def countAddHand[CL](cls: Class[CL]) =
      count(stateCurrent.hand, cls) - count(stateOld.hand, cls)

    if (countAddBattle(classOf[LotusPetal]) > 0)
      sum += 1.1
    if (countAddLands(classOf[Permanent]) > 0)
      sum += 1
    if (countAddHand(classOf[Duress]) < 0 && count(stateCurrent.hand, classOf[Duress]) == 0)
      sum -= 3

    sum += stateCurrent.lands.getSeq.size * 1
    sum += stateCurrent.battlefield.getSeq.size * 0.5
    sum += countWinCard(stateCurrent.battlefield.getSeq) * 1000.0
    if (stateCurrent.numberTurn != stateOld.numberTurn) {
      sum -= stateCurrent.manaPool.getSeq.size
    }
    sum
  }
}