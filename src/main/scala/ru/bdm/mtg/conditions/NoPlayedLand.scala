package ru.bdm.mtg.conditions
import ru.bdm.mtg.State

object NoPlayedLand extends Condition {
  override def check(state: State): Boolean =
    !state.playedLand
}
