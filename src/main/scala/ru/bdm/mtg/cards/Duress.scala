package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}

class Duress extends Card {

  override def isPlayable(current: State): Boolean = basicIsPlayable(current, "B")

  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(hand = current.hand - this, manaPool = current.manaPool - 'B'))
  }
}
