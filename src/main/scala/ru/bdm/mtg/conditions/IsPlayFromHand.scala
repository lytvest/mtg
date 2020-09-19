package ru.bdm.mtg.conditions

import ru.bdm.mtg.{Card, State}

case class IsPlayFromHand(card:Card) extends Condition{
  override def check(state: State): Boolean =
    IsPlay.check(state) && state.hand.contains(card)
}
