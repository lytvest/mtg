package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, State}

case class RemoveFromHandAndMana(card:Card, cost:String) extends Action{
  override def act(state: State): Seq[State] = {
    (RemoveMana(cost) * RemoveFromHand(card)).act(state)
  }
}
