package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddDiscard, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, CountInHand, Discard, IsPlayFromHandAndMana}

case class ThrillOfPossibility() extends Card {
  val cost: String = "CR"

  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHandAndMana(this, cost) and CountInHand(_ > 1)) -> TakeCards(2) * RemoveFromHandAndMana(this, cost) * AddDiscard(1),
    Discard.standard(this)
  )
}
