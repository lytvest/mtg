package ru.bdm.mtg.lands

import ru.bdm.mtg.{Land, State}

class Mountain(active: Boolean = true) extends Land(active) {


  def tap(current:State) : Seq[State] = {
    Seq(current.copy(manaPool = current.manaPool + 'R', lands = (current.lands - this) + new Mountain(false)))
  }
}
