package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}

object RandomCard extends Card{
  override def nextStates(current: State): Seq[State] = ???
}
