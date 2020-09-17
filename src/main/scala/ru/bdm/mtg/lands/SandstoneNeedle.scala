package ru.bdm.mtg.lands

import ru.bdm.mtg.{Land, State}
import ru.bdm.mtg.AllSet._

class SandstoneNeedle(active:Boolean = false, count:Int = 2) extends Land(active) {

  override def tap(current: State): Seq[State] = {
    if(count - 1 > 0)
      Seq(current.copy(manaPool = current.manaPool ++~ "RR",
        lands = (current.lands - this) +~ new SandstoneNeedle(false, count - 1)))
    else {
      Seq(current.copy(manaPool = current.manaPool ++~ "RR",
        lands = current.lands - this))
    }
  }

  override def copy(active: Boolean): Land = new SandstoneNeedle(active, count)
}
