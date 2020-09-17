package ru.bdm.mtg

import scala.io.StdIn

class ConsolePlayer extends Player{
  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    println(outcomes.zipWithIndex.mkString("Выбери карту:\n", "\n", "Введите -1 для следующего хода"))
    StdIn.readInt()
  }
}
