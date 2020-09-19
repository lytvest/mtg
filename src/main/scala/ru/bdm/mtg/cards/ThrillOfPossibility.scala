package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.actions.{Action, AddDiscard, AddGraveyard, DiscardCard, RemoveFromGraveyard, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, InGraveyard, IsPlay, IsPlayFromHandAndMana, CanPay}
import ru.bdm.mtg.{Card, ManaPool, State}

class ThrillOfPossibility extends Card  {
  val cost: String = "CR"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(2) * RemoveFromHandAndMana(this, cost) * AddDiscard(1),
    Discard.standard(this)
  )
}
