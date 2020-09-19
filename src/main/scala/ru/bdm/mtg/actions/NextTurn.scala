package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.lands.Land
import ru.bdm.mtg.{AllSet, State}

object NextTurn extends Action {
  override def act(state: State): Seq[State] =
    Seq(state.copy(
      manaPool = AllSet.empty[Char],
      numberTurn = state.numberTurn + 1,
      takeCards = state.takeCards + 1,
      lands = AllSet.empty[Land] ++~ state.lands.getSeq.map(_.copy(true)),
      playedLand = false
    ))
}
