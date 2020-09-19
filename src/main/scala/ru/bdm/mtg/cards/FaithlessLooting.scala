package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, AddGraveyard, DiscardCard, RemoveFromGraveyard, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, InGraveyard, IsPlay, IsPlayFromHandAndMana, Mana}
import ru.bdm.mtg.{Card, State}

class FaithlessLooting() extends Card  {

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "R") -> TakeCards(2) * RemoveFromHandAndMana(this, "R") * AddGraveyard(this),
    (IsPlay and InGraveyard(this) and Mana("CCR")) -> TakeCards(2) * RemoveFromGraveyard(this) * RemoveMana("CCR"),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
