package ru.bdm.mtg

import scala.io.StdIn

class ConsolePlayer extends Agent{
  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    println("Текущее состояние:\n" + current + "\n")
    print(outcomes.zipWithIndex.map{case(f,s) => (s,current.getChanges(f))}.mkString("Выбери следующие состояние:\n", "\n", "\n--->"))
    if (outcomes.length > 1)
      StdIn.readInt()
    else {
      println(0)
      0
    }
  }


}
