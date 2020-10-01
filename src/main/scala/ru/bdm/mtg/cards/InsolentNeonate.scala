package ru.bdm.mtg.cards

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._
import ru.bdm.mtg.lands.Permanent

case class InsolentNeonate(override val active: Boolean = false) extends Permanent(active){
  val cost = "R"

  override lazy val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    (IsPlay and IsBattlefield(this) and CountInHand(_ > 1)) -> AddGraveyard(this.copy(false)) * RemoveFromBattlefield(this) * TakeCards(1) * AddDiscard(1),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this.copy(false)),
  )

  override def copy(active: Boolean): Permanent = InsolentNeonate(active)
}
