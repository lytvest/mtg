package ru.bdm.mtg

abstract class Land(val active: Boolean = false) extends Card {
  override def nextStates(current: State): Seq[State] = {
    if(current.hand.contains(this)){
      Seq(current.copy(hand = current.hand - this, lands = current.lands + this))
    } else {
      tap(current)
    }
  }
  override def isPlayable(current:State): Boolean = {
    true
  }
  def tap(state: State): Seq[State]

}
