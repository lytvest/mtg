package ru.bdm.mtg.lands

import ru.bdm.mtg.State
import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

case class Thriving(meColor: Char, restColor: String, override val active: Boolean = false, choose: Option[Char] = None) extends Permanent(active) {

  override lazy val description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) ->
      ((state: State) =>
        restColor flatMap { pool =>
          RemoveFromHand(this) * AddLand(Thriving(meColor, restColor, active, Some(pool))) act state
        }
        ),
    IsTappedLand(this) -> Tap(this) * (AddMana("" + meColor) or LazyAction(() => AddMana("" + choose.get))),
    Discard.standard(this)
  )

  override def copy(active: Boolean): Permanent = Thriving(meColor, restColor, active, choose)

  override def name: String = super.name + meColor + choose.getOrElse("")
}
