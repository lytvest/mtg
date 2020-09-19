package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.actions.{Action, AddBattlefield, AddGraveyard, DiscardCard, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

class UlamogsCrusher extends Card  {
  val cost: String = "CCCCCCCC"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
