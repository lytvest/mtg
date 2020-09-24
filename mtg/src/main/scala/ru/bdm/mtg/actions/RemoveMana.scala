package ru.bdm.mtg.actions
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.State

case class RemoveMana(cost:String) extends Action {
  override def act(state: State): Seq[State] = {
    state.manaPool.pay(cost) map (pool => state.copy(manaPool = pool))
  }
}
