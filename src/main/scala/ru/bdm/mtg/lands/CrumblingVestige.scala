package ru.bdm.mtg.lands

import ru.bdm.mtg.{Land, State}
import ru.bdm.mtg.AllSet._

class CrumblingVestige(active:Boolean = false) extends Land(active) {

  private def newState(current:State, color:Char) = {
    current.copy(hand = current.hand - this,
      lands = current.lands +~ this,
    manaPool = current.manaPool +~ color)
  }

  override def nextStates(current: State): Seq[State] = {
    if(current.hand.contains(this)){
      "WUBR".map(newState(current, _))
    } else {
      tap(current)
    }
  }

  override def tap(current: State): Seq[State] = {
    Seq(current.copy(manaPool = current.manaPool +~ 'C',
      lands = (current.lands - this) +~ new CrumblingVestige(false))
    )
  }

  override def copy(active: Boolean): Land = new CrumblingVestige(active)
}
