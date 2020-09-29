package ru.bdm.mtg.lands

import ru.bdm.mtg.Card

abstract class Permanent(val active: Boolean = false) extends Card {

  override def equals(other: Any): Boolean = other match {
    case that: Permanent =>
      super.equals(that) &&
        active == that.active
    case _ => false
  }

  def copy(active: Boolean = this.active): Permanent

  override def toString: String = super.toString + "(" + active + ")"
}
