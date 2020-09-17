package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards.RandomCard

class Engine(player: Player) {
  var currentState = State()
  var number = 0
  var wasLandDrop = false

  def one() = {
    val outcomes = getOutcomes
    val index = player.chooseState(currentState, outcomes)
    if (index >= 0)
      currentState = outcomes(index)
    else
      startCourse()
  }

  def startCourse(): Unit = {
    val numAdd = 1 + currentState.hand.getOrElse(RandomCard, 0)

    currentState = currentState.copy (
      hand = (currentState.hand - RandomCard) ++~ currentState.library.slice(0, numAdd),
      library = currentState.library.drop(numAdd),
      lands = currentState.lands.map{ case (land, number) => land.copy(true) -> number}
    )
  }

  def getOutcomes: Seq[State] = {
    currentState.hand.keys.filter(_.isPlayable(currentState))
      .flatMap(_.payMana(currentState, removedHand = true)) ++
      currentState.graveyard.keys.filter(_.isPlayable(currentState))
        .flatMap(_.payMana(currentState, removedHand = false)) toSeq
  }

}
