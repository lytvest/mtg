package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._

class CarefulStudy extends Card {
  override def cost: String = "U"

  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(hand = current.hand +~ RandomCard +~ RandomCard, discard = current.discard + 2))
  }
}
