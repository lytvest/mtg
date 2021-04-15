package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}
import ru.bdm.mtg.cards.{HandOfEmrakul, UlamogsCrusher}

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
    new ConsolePlayerBattle()
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