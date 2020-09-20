package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.conditions.Discard
import ru.bdm.mtg.lands.Land
import ru.bdm.mtg.{AllSet, State}

object NextTurn extends Action {
  override def act(state: State): Seq[State] = {
    val isDrop = state.hand.values.sum <= 7
    val seq = Seq(state.copy(
      manaPool = AllSet.empty[Char],
      numberTurn = state.numberTurn + 1,
      takeCards = if(isDrop) 1 else 0,
      lands = AllSet.empty[Land] ++~ state.lands.getSeq.map(_.copy(true)),
      playedLand = false
    ))

    if(!isDrop){
      seq.flatMap{ state =>
        val card = state.library.head
        card.description(Discard(card)).act(state.copy(library = state.library.tail))
      }
    } else
      seq
  }
}
