package ru.bdm.mtg.lands

import ru.bdm.mtg.{Land, State}
import ru.bdm.mtg.AllSet._

class Thriving(val meColor:Char, val restColor:String, active:Boolean = false, choose:Option[Char] = None) extends Land(active){

  private def newState(current:State, color:Char) = {
    current.copy(hand = current.hand - this,
      lands = current.lands +~ new Thriving(meColor, restColor, false, Some(color)),
      )
  }

  override def nextStates(current: State): Seq[State] = {
    if(current.hand.contains(this)){
      restColor.map(newState(current, _))
    } else {
      tap(current)
    }
  }
  private def cre(current:State, color: Char) = {
    current.copy(manaPool = current.manaPool +~ color,
      lands = (current.lands - this) +~ new Thriving(meColor, restColor, false, choose)
    )
  }

  override def tap(state: State): Seq[State] = {
    Seq(cre(state, meColor), cre(state, choose.get))
  }

  override def copy(active: Boolean): Land = new Thriving(meColor, restColor, active, choose)

  override def name: String = super.name + meColor
}
