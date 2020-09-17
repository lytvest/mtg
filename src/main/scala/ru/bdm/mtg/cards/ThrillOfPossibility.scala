package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, ManaPool, State}

class ThrillOfPossibility extends Card {
  override def cost: String = "CR"

  override def nextStates(current: State): Seq[State] = {
    current.hand getCombinations(1) map { discarded =>
      current.copy(hand = current.hand +~ RandomCard +~ RandomCard --~ discarded.keys.toSeq)
    }
  }
}
