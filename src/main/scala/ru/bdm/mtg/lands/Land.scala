package ru.bdm.mtg.lands

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

abstract class Land(val active: Boolean = false) extends Card {

  override def equals(other: Any): Boolean = other match {
    case that: Land =>
      super.equals(that) &&
        active == that.active
    case _ => false
  }
  def copy(active:Boolean = this.active): Land

  override def toString: String = super.toString + "(" + active + ")"
}
