package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps

abstract class Card {
  def nextStates(current: State):Seq[State]
  def isPlayable(current: State): Boolean = basicIsPlayable(current, cost)

  def basicIsPlayable(current: State, cost:String): Boolean = {
    current.manaPool.canPay(cost)
  }
  def cost:String = ""

  def payMana(current:State, removedHand:Boolean):Seq[State] = {
    current.manaPool.pay(cost) flatMap { pool =>
      nextStates(current.copy(manaPool = pool,
        hand = if(removedHand) current.hand -~ this else current.hand))
    }
  }

  def discard(current:State): State = current

  def name:String =
    getClass.getSimpleName

  def *(num:Int): Seq[Card] =
    Seq.fill(num)(this)


  override def equals(other: Any): Boolean = other match {
    case that: Card =>
        name == that.name
    case _ => false
  }

  override def hashCode(): Int = {
    name.hashCode()
  }
}
