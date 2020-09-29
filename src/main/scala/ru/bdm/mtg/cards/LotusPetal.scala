package ru.bdm.mtg.cards

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._
import ru.bdm.mtg.lands.Permanent

case class LotusPetal(override val active: Boolean = true) extends Permanent(active) {

  override val description: Map[Condition, Action] = Map(
    (IsPlay and InHand(this)) -> RemoveFromHand(this) * AddBattlefield(this),
    (IsPlay and IsBattlefield(this)) -> RemoveFromBattlefield(this) * AddDifferentColors("WUBR", 1),
    Discard.standard(this)
  )

  override def copy(active: Boolean): Permanent = LotusPetal(active)
}
