package ru.bdm.mtg.conditions
import ru.bdm.mtg.State

case class CountInHand(fun: Int => Boolean) extends Condition{
  override def check(state: State): Boolean =
    fun(state.hand.values.sum)
}
