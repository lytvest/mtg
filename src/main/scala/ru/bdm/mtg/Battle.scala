package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}

class Battle(val deck: AllSet.Type[Card], player: Agent) {
  private val shDeck = Deck.shuffleTheDeck(deck getSeq)

  var currentState: State = State(hand = AllSet.empty[Card] ++~ shDeck.slice(0, 7), library = shDeck.drop(7))


  def run(): Unit = {
    while (currentState.numberTurn < 30) {
      while (currentState.takeCards > 0) {
        setPhase(Phase.take)
        val actives = getActiveActions
        if (actives.isEmpty)
          takeCards
        else {
          choosePlayerState(actives)
        }
      }
      while (currentState.discard > 0) {
        setPhase(Phase.discard)
        val actives = getActiveActions
        choosePlayerState(actives)
      }
      setPhase(Phase.play)
      val actives = getActiveActions :+ NextTurn
      choosePlayerState(actives)
    }
  }

  private def setPhase(phase: String): Unit = {
    currentState = currentState.copy(phase = phase)
  }

  private def choosePlayerState(actives: Seq[Action]): Unit = {
    val choose = actives.flatMap(_.act(currentState))
    val index = player.chooseState(currentState, choose)
    currentState = choose(index)
  }

  private def takeCards(): Unit = {
    currentState = currentState.copy(hand = currentState.hand ++~ currentState.library.slice(0, currentState.takeCards),
      library = currentState.library.drop(currentState.takeCards),
      takeCards = 0)
  }

  private def getActiveActions:Seq[Action] = {
    allCards.flatMap(_.description.filter{ case (condition, action) =>
      condition.check(currentState)
    }) map(_._2) toSeq
  }

  private def allCards: Iterable[Card] =
    currentState.hand.keys ++ currentState.lands.keys ++ currentState.graveyard.keys

}
