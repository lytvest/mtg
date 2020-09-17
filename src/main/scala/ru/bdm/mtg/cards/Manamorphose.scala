package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, ManaPool, State}

class Manamorphose extends Card {
  override def cost: String = "CR"

  override def nextStates(current: State): Seq[State] = {
    ManaPool("WWUUBBRR") getCombinations(2) map { pool =>
      current.copy(manaPool = current.manaPool ++ pool, hand = current.hand +~ RandomCard)
    }
  }
}
