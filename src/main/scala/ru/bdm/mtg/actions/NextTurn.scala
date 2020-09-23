package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.conditions.Discard
import ru.bdm.mtg.lands.Land
import ru.bdm.mtg.{AllSet, Phase, State}

object NextTurn extends Action {
  override def act(state: State): Seq[State] = {
    val drop = state.hand.values.sum - 7
    val seq = Seq(state.copy(
      manaPool = AllSet.empty[Char],
      numberTurn = state.numberTurn + 1,
      takeCards = 1,
      discard = state.endTurnDiscards + Math.max(0, drop),
      endTurnDiscards = 0,
      lands = AllSet.empty[Land] ++~ state.lands.getSeq.map(_.copy(true)),
      playedLand = false,
      phase = Phase.discardFirst
    ))
    seq
  }
}
