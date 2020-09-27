package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class HandOfEmrakul() extends Card {
  val cost: String = "CCCCCCCCC"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
