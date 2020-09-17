package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._
class DarkRitual extends Card{
  override def cost = "B"
  override def nextStates(current: State): Seq[State] = {
    Seq(current.copy(manaPool = current.manaPool ++~ "BBB"))
  }

}
