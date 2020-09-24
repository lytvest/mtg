package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddDiscard, AddGraveyard, DiscardCard, RemoveFromGraveyard, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{CanPay, Condition, Discard, InGraveyard, IsPlay, IsPlayFromHandAndMana}

class DeepAnalysis()extends Card {
   val cost = "CCCU"
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CCCU") -> TakeCards(2) * RemoveFromHandAndMana(this, "CCCU") * AddGraveyard(this),
    (IsPlay and InGraveyard(this) and CanPay("CU")) ->  TakeCards(2) * RemoveFromGraveyard(this) * RemoveMana("CU"),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
