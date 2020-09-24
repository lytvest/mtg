package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.State

case class AddMana(cost: String) extends Action{
  override def act(state: State): Seq[State] =
    Seq(state.copy(manaPool = state.manaPool ++~ cost))
}
