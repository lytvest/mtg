package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.cards.DragonBreath

case object HasteChoose extends Action{
  override def act(state: State): Seq[State] =
    state.battlefield.keys.filter(_.active).map{ chooseCard =>
      state.copy(battlefield = state.battlefield -~ chooseCard +~ chooseCard.copy(true))
    }.toSeq
}
