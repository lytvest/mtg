package ru.bdm.mtg.conditions
import ru.bdm.mtg.State

object True extends Condition{
  override def check(state: State): Boolean = true
}
