package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

case class FaithlessLooting() extends Card {

  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHandAndMana(this, "R") and CountInHand(_ > 2)) -> AddDiscard(2) * TakeCards(2) * RemoveFromHandAndMana(this, "R") * AddGraveyard(this),
    (IsPlay and InGraveyard(this) and CanPay("CCR")) -> AddDiscard(2) * TakeCards(2) * RemoveFromGraveyard(this) * RemoveMana("CCR"),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )
}
