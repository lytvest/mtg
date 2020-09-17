package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.{AllSetOps, empty}
import ru.bdm.mtg.{AllSet, Card, State}

class DangerousWager extends Card {
  override def cost: String = "CU"

  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(hand = empty[Card] ++~ (RandomCard * 2)))
  }
}
