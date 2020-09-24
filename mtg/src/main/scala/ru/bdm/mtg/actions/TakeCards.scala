package ru.bdm.mtg.actions
import ru.bdm.mtg.{Phase, State}

case class TakeCards(num:Int) extends Action {
  override def act(state: State): Seq[State] =
    Seq(state.copy(takeCards = state.takeCards + num, phase = Phase.takeFirst))
}
