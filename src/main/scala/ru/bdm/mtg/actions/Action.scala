package ru.bdm.mtg.actions

import ru.bdm.mtg.State

trait Action extends Serializable {
  def act(state:State):Seq[State]

  def *(action: Action): Action =
    state => this.act(state).flatMap(action.act)

  def or(action: Action):Action =
    state => this.act(state) ++ action.act(state)
}
