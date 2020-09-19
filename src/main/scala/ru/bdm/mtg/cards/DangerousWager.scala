package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, RemoveFromHand, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

class DangerousWager extends Card {
  val cost = "CR"
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(2) * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
