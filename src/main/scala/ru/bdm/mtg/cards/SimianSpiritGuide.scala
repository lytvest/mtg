package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._

class SimianSpiritGuide extends Card {
  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(hand = current.hand - this, manaPool = current.manaPool +~ 'R'))
  }

  override def isPlayable(current: State): Boolean = true
}
