package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddDifferentColors, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class Manamorphose() extends Card {
  val cost: String = "CR"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> AddDifferentColors("WWUUBBRR", 2) * TakeCards(1) * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
