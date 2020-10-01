package ru.bdm.mtg.cards

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.lands.Permanent

case class UlamogsCrusher(override val active: Boolean = false) extends Permanent(active) {
  val cost: String = "CCCCCCCC"

  override lazy val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this.copy(false))
  )

  override def copy(active: Boolean): Permanent = UlamogsCrusher(active)
}
