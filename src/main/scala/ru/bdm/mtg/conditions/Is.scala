package ru.bdm.mtg.conditions

import ru.bdm.mtg.State

case class Is(predicate: Boolean) extends Condition {

  override def check(state: State): Boolean =
    predicate
}
