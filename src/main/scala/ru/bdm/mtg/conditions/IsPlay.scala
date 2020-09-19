package ru.bdm.mtg.conditions
import ru.bdm.mtg.{Phase, State}

object IsPlay extends Condition{
  override def check(state: State): Boolean =
    state.phase == Phase.play
}
