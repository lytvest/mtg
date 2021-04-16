package ru.bdm.mtg

import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.actions.Action
import ru.bdm.mtg.conditions.Condition

trait Card extends Serializable {

  def name: String =
    getClass.getSimpleName

  def description: Map[Condition, Action]

  def basicIsPlayable(current: State, cost: String): Boolean = {
    current.manaPool.canPay(cost)
  }

  def *(num: Int): Seq[Card] =
    Seq.fill(num)(this)

  override def equals(other: Any): Boolean = other match {
    case that: Card =>
      name == that.name
    case _ => false
  }

  override def hashCode(): Int = {
    name.hashCode()
  }

  override def toString: String = s"$name"
}
