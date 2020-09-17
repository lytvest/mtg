package ru.bdm.mtg.lands

import ru.bdm.mtg.{Land, State}
import ru.bdm.mtg.AllSet._

class PeatBog(active:Boolean = false, count:Int = 2) extends Land(active) {

  override def tap(current: State): Seq[State] = {
    if(count - 1 > 0)
      Seq(current.copy(manaPool = current.manaPool ++~ "BB",
        lands = (current.lands - this) +~ new PeatBog(false, count - 1)))
    else {
      Seq(current.copy(manaPool = current.manaPool ++~ "BB",
        lands = current.lands - this))
    }
  }

}
