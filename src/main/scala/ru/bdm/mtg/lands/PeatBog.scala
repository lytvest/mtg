package ru.bdm.mtg.lands

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

class PeatBog(active: Boolean = false, count: Int = 2) extends Land(active) {

  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) -> AddLand(this) * RemoveFromHand(this),
    IsTappedLand(this) -> AddMana("BB") * TemporaryLand(this, count, new PeatBog(false, _)),
    Discard.standard(this)
  )

  override def copy(active: Boolean): Land = new PeatBog(active, count)

  override def toString: String = super.toString + s"[$count]"
}
