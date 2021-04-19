package ru.bdm.mtg.cards

import ru.bdm.mtg.actions.{Action, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

case class Ponder() extends Card {

  object PonderAction extends Action {

    val комбинации = Seq(Seq(0, 1, 2), Seq(0, 2, 1), Seq(1, 0, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(2, 1, 0))

    override def act(state: State): Seq[State] = {
      val первыеТриКарты = (state.topOfLibrary ++ state.library).slice(0, 3)

      комбинации.map(_.map(первыеТриКарты(_))).map { seq =>
        state.copy(
          topOfLibrary = seq ++ state.topOfLibrary.drop(3),
          library = state.library.drop(Math.max(3 - state.topOfLibrary.size, 0)),
        )
      } :+ state.copy(shuffle = true)

    }
  }

  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "U") -> PonderAction * TakeCards(1) * RemoveFromHandAndMana(this, "U"),
    Discard.standard(this)
  )
}
