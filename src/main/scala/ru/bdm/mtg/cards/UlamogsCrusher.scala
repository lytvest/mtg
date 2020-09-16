package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}

class UlamogsCrusher extends Card{
  override def nextStates(current: State): Seq[State] = {

  }

  override def isPlayable(current: State): Boolean = basicIsPlayable(current, "CCCCCCCC")
}
