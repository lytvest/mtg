package ru.bdm.mtg

abstract class Card {
  def nextStates(current: State):Seq[State]
  def isPlayable(current: State): Boolean

  def basicIsPlayable(current: State, cost:String): Boolean = {
    cost.foldRight(true) { case (ch, value) =>
      current.manaPool.contains(ch) && value
    }
  }

  override def equals(obj: Any): Boolean = {
    obj == this || getClass == obj.getClass
  }
}
