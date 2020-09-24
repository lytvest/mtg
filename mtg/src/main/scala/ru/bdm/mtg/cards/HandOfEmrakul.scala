package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.actions.{Action, AddBattlefield, AddGraveyard, DiscardCard, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

class HandOfEmrakul extends Card  {
  val cost: String = "CCCCCCCCC"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
