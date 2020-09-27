package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddEndTurnDiscard, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class IdeasUnbound() extends Card {
  val cost = "UU"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> AddEndTurnDiscard(3) * TakeCards(3) * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
