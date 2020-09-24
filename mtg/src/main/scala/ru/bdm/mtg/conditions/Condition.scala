package ru.bdm.mtg.conditions

import ru.bdm.mtg.State

trait Condition {

  def check(state: State): Boolean

  def and(condition: Condition): Condition =
    (state: State) => this.check(state) && condition.check(state)

  def or(condition: Condition): Condition =
    (state: State) => this.check(state) || condition.check(state)
}


