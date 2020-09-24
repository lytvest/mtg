package ru.bdm.mtg.conditions
import ru.bdm.mtg.State

object False extends Condition {
  override def check(state: State): Boolean = false
}
