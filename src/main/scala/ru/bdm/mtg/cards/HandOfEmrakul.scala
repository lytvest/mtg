package ru.bdm.mtg.cards

import ru.bdm.mtg.actions.{Action, AddBattlefield, AddGraveyard, DiscardCard, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.lands.Permanent

case class HandOfEmrakul(override val active: Boolean = false) extends Permanent(active) {
  val cost: String = "CCCCCCCCC"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )

  override def copy(active: Boolean): Permanent = HandOfEmrakul(active)
}
