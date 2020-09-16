package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}

class DarkRitual extends Card{
  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(manaPool = current.manaPool ++ "BB", hand = current.hand - this))
  }

  override def isPlayable(current: State): Boolean = basicIsPlayable(current, "B")
}
