package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}

class Battle(val deck: AllSet.Type[Card], player: Agent) {
  private val shDeck = Deck.shuffleTheDeck(deck.getSeq)

  var currentState: State = State(hand = AllSet.empty[Card] ++~ shDeck.slice(0, 7), library = shDeck.drop(7))


  def run(): Unit = {
    while (currentState.numberTurn < 30) {
      if (currentState.phase == Phase.takeFirst)
        takeAll()
      if (currentState.phase == Phase.discardFirst)
        discardAll()
      takeAll()
      discardAll()
      currentState = currentState.copy(discard = 0, takeCards = 0)

      shuffleLibrary()

      setPhase(Phase.play)
      val actives = getActiveActions :+ NextTurn
      choosePlayerState(actives)
    }
  }

  private def shuffleLibrary(): Unit = {
    if (currentState.shuffle)
      currentState = currentState.copy(
        library = Deck.shuffleTheDeck(currentState.library ++ currentState.верхКолоды),
        верхКолоды = Seq.empty[Card],
        shuffle = false
      )
  }

  private def discardAll(): Unit = {
    while (currentState.discard > 0 && currentState.hand.nonEmpty) {
      setPhase(Phase.discard)
      val actives = getActiveActions
      choosePlayerState(actives)
    }
  }

  private def takeAll(): Unit = {
    while (currentState.takeCards > 0) {
      setPhase(Phase.take)
      val actives = getActiveActions
      if (actives.isEmpty)
        takeCards()
      else {
        choosePlayerState(actives)
      }
    }
  }

  private def setPhase(phase: String): Unit = {
    currentState = currentState.copy(phase = phase)
  }

  private def choosePlayerState(actives: Seq[Action]): Unit = {
    val choose = actives.flatMap(_.act(currentState)).distinct
    if(choose.nonEmpty) {
      val index = player.chooseState(currentState, choose)
      currentState = choose(index)
    }
  }

  private def takeCards(): Unit = {
    val lib = currentState.верхКолоды ++ currentState.library
    currentState = currentState.copy(
      hand = currentState.hand ++~ lib.slice(0, currentState.takeCards),
      верхКолоды = currentState.верхКолоды.drop(currentState.takeCards),
      library = currentState.library.drop(Math.max(currentState.takeCards - currentState.верхКолоды.size, 0)),
      takeCards = 0)
  }

  private def getActiveActions:Seq[Action] = {
    allCards.flatMap(_.description.filter{ case (condition, action) =>
      condition.check(currentState)
    }).map(_._2).toSeq
  }

  private def allCards: Iterable[Card] =
    currentState.hand.keys ++ currentState.lands.keys ++ currentState.graveyard.keys ++ currentState.battlefield.keys

}
