package ru.bdm.mtg

import scala.io.StdIn

class ConsolePlayer extends Player{
  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    println("Текущее состояние:\n" + current + "\n")
    println(outcomes.zipWithIndex.map{case(f,s) => (s,current.getChanges(f))}.mkString("Выбери следующие состояние:\n", "\n", "\nВведите -1 для следующего хода"))
    StdIn.readInt()
  }


}
