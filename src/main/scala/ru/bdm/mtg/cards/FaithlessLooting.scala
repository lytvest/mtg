package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, AddDiscard, AddGraveyard, DiscardCard, RemoveFromGraveyard, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{CanPay, Condition, Discard, InGraveyard, IsPlay, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

class FaithlessLooting() extends Card  {

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "R") -> TakeCards(2) * AddDiscard(2) * RemoveFromHandAndMana(this, "R") * AddGraveyard(this),
    (IsPlay and InGraveyard(this) and CanPay("CCR")) -> TakeCards(2) * AddDiscard(2) * RemoveFromGraveyard(this) * RemoveMana("CCR"),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
