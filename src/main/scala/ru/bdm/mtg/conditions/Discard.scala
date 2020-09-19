package ru.bdm.mtg.conditions
import ru.bdm.mtg.actions.{Action, DiscardCard, RemoveFromHand}
import ru.bdm.mtg.{Card, Phase, State}

case class Discard(card:Card) extends Condition {
  override def check(state: State): Boolean =
    state.phase == Phase.discard && InHand(card).check(state)
}
object Discard{
  def standard(card:Card):(Condition, Action) =
    Discard(card) ->  DiscardCard(card)
}
