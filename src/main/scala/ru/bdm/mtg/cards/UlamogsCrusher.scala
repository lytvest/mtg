package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._

class UlamogsCrusher extends Card{
  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(battlefield = current.battlefield +~ this, hand = current.hand -~ this))
  }

  override def isPlayable(current: State): Boolean = basicIsPlayable(current, "CCCCCCCC")
}
