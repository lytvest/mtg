package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._

class Duress extends Card {
  override def cost: String = "B"

  override def nextStates(current: State): Seq[State] = {
    Seq(current)
  }
}
