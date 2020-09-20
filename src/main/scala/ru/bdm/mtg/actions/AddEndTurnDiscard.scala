package ru.bdm.mtg.actions
import ru.bdm.mtg.State

case class AddEndTurnDiscard(num:Int) extends Action{
  override def act(state: State): Seq[State] =
    state.copy(endTurnDiscards = state.endTurnDiscards + num)
}
