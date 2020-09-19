package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, RemoveFromHand, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

class DangerousWager extends Card {
  val cost = "CU"
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(2) * RemoveFromHand(this) * RemoveMana(cost),
    Discard.standard(this)
  )
}
