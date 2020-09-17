package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._

class CarefulStudy extends Card with Discarded {
  override def cost: String = "U"

  override def numberDiscard: Int = 2

  override def states(current: State, cards: Seq[Card]): Seq[State] =
    Seq(current.copy(hand = current.hand ++~ (RandomCard * 2)))
}
