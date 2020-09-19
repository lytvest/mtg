package ru.bdm.mtg.conditions

import ru.bdm.mtg.{Card, State}

case class InGraveyard(card:Card) extends Condition{
  override def check(state: State): Boolean =
    state.graveyard.contains(card)
}
