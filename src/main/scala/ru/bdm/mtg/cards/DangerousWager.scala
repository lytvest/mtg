package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, DiscardHand, RemoveFromHand, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class DangerousWager() extends Card {
  val cost = "CR"
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(2) * DiscardHand * RemoveMana(cost),
    Discard.standard(this)
  )
}
