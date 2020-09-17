package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, ManaPool, State}

class CatharticReunion extends Card with Discarded {
  override def cost: String = "CR"

  override def numberDiscard: Int = 2

  override def states(current: State, cards: Seq[Card]): Seq[State] =
    Seq(current.copy(hand = current.hand ++~ (RandomCard * 3)))
}
