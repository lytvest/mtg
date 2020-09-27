package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddDiscard, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, CountInHand, Discard, IsPlayFromHandAndMana}

case class CatharticReunion() extends Card {
  val cost: String = "CR"

  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHandAndMana(this, cost) and CountInHand(_ > 2)) -> TakeCards(3) * AddDiscard(2) * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
