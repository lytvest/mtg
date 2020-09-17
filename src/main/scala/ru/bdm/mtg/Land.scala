package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps

abstract class Land(val active: Boolean = false) extends Card {
  override def nextStates(current: State): Seq[State] = {
    if (current.hand.contains(this)) {
      Seq(current.copy(hand = current.hand - this, lands = new AllSetOps(current.lands) +~ this))
    } else {
      tap(current)
    }
  }

  override def isPlayable(current: State): Boolean = {
    true
  }

  def tap(state: State): Seq[State]

  override def equals(other: Any): Boolean = other match {
    case that: Land =>
      super.equals(that) &&
        active == that.active
    case _ => false
  }
  def copy(active:Boolean = this.active): Land
}
