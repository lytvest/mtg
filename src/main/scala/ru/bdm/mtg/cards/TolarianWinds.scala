package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.{AllSetOps, empty}
import ru.bdm.mtg.{Card, State}

class TolarianWinds extends Card {
  override def cost: String = "CU"

  override def nextStates(current: State): Seq[State] = {
    val cards = current.hand.values.sum
    Seq(current.copy(hand = Map(RandomCard -> cards)))
  }
}
