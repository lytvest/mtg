package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, State}

class HandOfEmrakul extends Card with DiscardToGraveyard {
  override def cost: String = "CCCCCCCCC"
  override def nextStates(current: State): Seq[State] = {
    current.manaPool pay (cost) map { pool =>
      current.copy(battlefield = current.battlefield +~ this, manaPool = pool)
    }
  }
}
