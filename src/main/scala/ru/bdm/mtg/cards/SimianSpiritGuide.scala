package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}

class SimianSpiritGuide extends Card {
  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(hand = current.hand - this, manaPool = current.manaPool + 'R'))
  }

  override def isPlayable(current: State): Boolean = true
}
