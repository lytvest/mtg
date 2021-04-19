package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddMana, RemoveFromHand}
import ru.bdm.mtg.conditions.{Condition, Discard, InHand, IsPlayFromHandAndMana, CanPay}

case class DarkRitual() extends Card {

  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "B") -> AddMana("BB") * RemoveFromHand(this),
    Discard.standard(this)
  )
}
