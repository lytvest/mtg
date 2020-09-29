package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.lands.Permanent
import ru.bdm.mtg.{Card, State}

object Reanimation extends Action {
  val creatures: Set[String] = Set(
    "UlamogsCrusher",
    "HandOfEmrakul",
    "InsolentNeonate",
  )

  override def act(state: State): Seq[State] = {
    val res = state.graveyard.keys.filter{card =>
      creatures.contains(card.name)
    }.map(exhume(state, _)).toSeq
    if (res.isEmpty)
      Seq(state)
    else res
  }

  def exhume(current:State, choose:Card): State = {
    current.copy(
      battlefield = current.battlefield +~ choose.asInstanceOf[Permanent],
      graveyard = current.graveyard -~ choose
    )
  }
}
