package ru.bdm.mtg.conditions
import ru.bdm.mtg.State

case class Not(condition: Condition) extends Condition {
  override def check(state: State): Boolean = !condition.check(state)
}
