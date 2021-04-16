package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}
import ru.bdm.mtg.cards.{HandOfEmrakul, UlamogsCrusher}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, FileOutputStream, InputStream, ObjectOutputStream}
import java.sql.DriverManager
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, CanAwait, ExecutionContext, Future}
import scala.io.StdIn
import scala.io.StdIn.readLine


class ConsolePlayerBattle() {
  var state = BattleObserver.startState(DeckShuffler.allCard.getSeq)

  println("start battle")
  while (!BattleObserver.isEndStates(state)){
    println(state)
    val next = BattleObserver.nextStates(state)
    if (next.size > 1) {
      println("choose next state")
      next.zipWithIndex.foreach{ case (state, index) =>
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

object Main{
  def main(args: Array[String]): Unit = {
    new NP().start()
  }

}
class NP{
  var state = BattleObserver.startState(DeckShuffler.allCard.getSeq)
  implicit val executor = ExecutionContext.global


  var num_rec = 0
  val MB = 1024 * 1024.0
  val MAX_HASH_SIZE = 100_000_000
  var endsCount = 0

  val conn = DriverManager.getConnection("jdbc:h2:~/mtg-db", "bdm", "1234");

  val hashs = mutable.Map[State, Option[Int]]()
  var futures = List[Future[Unit]]()
  def addInDatabase(state: State, value: Int): Unit = {
    if(hashs.contains(state) && hashs(state).contains(value))
      return
    hashs(state) = Some(value)
    futures ::= Future {
      val sql = "insert into mtg (state, value) values (?, ?);"
      val st = conn.prepareStatement(sql)
      st.setBinaryStream(1, getStream(state))
      st.setInt(2, value)
      st.executeUpdate()
    }
  }
  def getStream(state: State) : InputStream = {
    val stream = new ByteArrayOutputStream()
    val objStream = new ObjectOutputStream(stream)
    objStream.writeObject(state)
    objStream.flush()
    objStream.close()
    new ByteArrayInputStream(stream.toByteArray)
  }

  def getFromDatabase(state: State): Option[Int] = {
    if (hashs.contains(state))
      return hashs(state)
    Await.ready(Future.sequence(futures), Duration.Inf)
    futures = Nil
    if(hashs.size > MAX_HASH_SIZE)
      hashs.clear()
    val sql = "select value from mtg where state =?"
    val pr = conn.prepareStatement(sql)
    pr.setBinaryStream(1, getStream(state))
    val set = pr.executeQuery()
    val res = if (set.next()) Some(set.getInt("value")) else None
    hashs(state) = res
    res
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

  def getTime(pr: Double): Double ={
    (System.currentTimeMillis() - start_time).toDouble / (10 * 60 * 60) / pr
  }

  def start(): Unit = {
    f(state, 0.0, 100)
  }

  def f(state: State, st_p: Double, len_p: Double): Int = {
    onPrint(st_p)
    val opt = getFromDatabase(state)
    if (opt.isDefined) {
      endPrint(state, opt.get)
      return opt.get
    }
    if(BattleObserver.isEndStates(state)){
      val res = if(BattleObserver.containsWinCard(state)) {
        addInDatabase(state, state.numberTurn)
        state.numberTurn
      } else {
        addInDatabase(state, Int.MaxValue)
        Int.MaxValue
      }
      endPrint(state, res)
      return res
    }
    num_rec += 1
    val next = BattleObserver.nextStates(state)
    val len = len_p / next.size
    val res = next.zipWithIndex.map{ case (s, i) =>
      f(s, st_p + i * len, len)
    }.min
    addInDatabase(state, res)
    num_rec -= 1
    res
  }


  private def onPrint(percent: Double) = {
    iter+=1
    if (iter % iterPrint == 0)
      println("-> " + num_rec  + " hash=" + hashs.size + " memory=" + memory + " / " + maxMemory + "   " + percent + "% time=" + getTime(percent))
  }

  private def endPrint(state: State, value: Int) = {
    endsCount += 1
    if (iter % iterPrint == 0)
      println( "-> " + (num_rec + 1) + " end turn=" + (if(value == Int.MaxValue) "fail" else value) + " ends=" + endsCount + " memory=" + memory + " / " + maxMemory)
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