package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.cards.DragonBreath

case class Aura(creator:Card => Card) extends Action{
  override def act(state: State): Seq[State] =
    state.battlefield.keys.map{ chooseCard =>
      state.copy(battlefield = state.battlefield -~ chooseCard +~ creator(chooseCard))
    }.toSeq
}
