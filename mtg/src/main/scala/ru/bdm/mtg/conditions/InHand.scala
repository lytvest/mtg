package ru.bdm.mtg.conditions

import ru.bdm.mtg.{Card, State}

case class InHand(card: Card) extends Condition {
  override def check(state: State): Boolean =
    state.hand.contains(card)
}
