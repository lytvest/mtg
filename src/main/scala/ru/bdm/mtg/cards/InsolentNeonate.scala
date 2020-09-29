package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddBattlefield, AddDiscard, AddGraveyard, DiscardCard, RemoveFromBattlefield, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, CountInHand, Discard, IsBattlefield, IsPlay, IsPlayFromHandAndMana}
import ru.bdm.mtg.lands.Permanent

case class InsolentNeonate(override val active: Boolean = false) extends Permanent(active){
  val cost = "R"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    (IsPlay and IsBattlefield(this) and CountInHand(_ > 1)) ->AddGraveyard(this) * RemoveFromBattlefield(this) * TakeCards(1) * AddDiscard(1),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this),
  )

  override def copy(active: Boolean): Permanent = InsolentNeonate(active)
}
