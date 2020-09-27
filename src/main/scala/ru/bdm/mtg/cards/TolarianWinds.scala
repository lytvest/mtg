package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, PayDiscardHand, RemoveMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class TolarianWinds() extends Card {
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CU") -> PayDiscardHand(this) * RemoveMana("CU"),
    Discard.standard(this)
  )
}
