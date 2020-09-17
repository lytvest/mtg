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

  def payMana(current:State):Seq[State] = {
    current.manaPool.pay(cost) flatMap { pool =>
      nextStates(current.copy(manaPool = pool, hand = current.hand -~ this))
    }
  }

  override def equals(obj: Any): Boolean = {
    obj == this || getClass == obj.getClass
  }

  def name:String =
    getClass.getSimpleName
}
