package ru.bdm.mtg.lands

import ru.bdm.mtg.{Land, State}
import ru.bdm.mtg.AllSet._

class Mountain(active: Boolean = true) extends Land(active) {


  def tap(current:State) : Seq[State] = {
    Seq(current.copy(manaPool = current.manaPool +~ 'R', lands = (current.lands - this) +~ new Mountain(false)))
  }

  override def copy(active: Boolean): Land = new Mountain(active)
}
