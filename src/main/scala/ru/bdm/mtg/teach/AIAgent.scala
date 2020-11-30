package ru.bdm.mtg.teach

import ru.bdm.mtg.{Agent, State}

class AIAgent() extends Agent{

  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    outcomes.map(_.score).zipWithIndex.max._2
  }
}
