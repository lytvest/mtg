package ru.bdm.mtg.conditions

import ru.bdm.mtg.{Card, State}

case class IsBattlefield(card: Card) extends Condition{
  override def check(state: State): Boolean =
    state.battlefield.contains(card)
}
