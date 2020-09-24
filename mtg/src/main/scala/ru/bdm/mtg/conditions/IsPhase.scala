package ru.bdm.mtg.conditions

import ru.bdm.mtg.{Phase, State}

case class IsPhase(phase: Phase.Phase) extends Condition{
  override def check(state: State): Boolean =
    state.phase == phase
}
