package ru.bdm.mtg.actions
import ru.bdm.mtg.State

case class LazyAction(action:() => Action) extends Action {
  override def act(state: State): Seq[State] =
    action().act(state)
}
