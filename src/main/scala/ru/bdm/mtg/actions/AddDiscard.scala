package ru.bdm.mtg.actions
import ru.bdm.mtg.State

case class AddDiscard(num:Int) extends Action {
  override def act(state: State): Seq[State] =
    Seq(state.copy(discard = state.discard + num))
}
