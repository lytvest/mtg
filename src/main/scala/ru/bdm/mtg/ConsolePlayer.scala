package ru.bdm.mtg

import ru.bdm.mtg.ConsolePlayer.readInt

import scala.io.StdIn

class ConsolePlayer extends Agent {
  override def name: String = "console"
  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    println("Текущее состояние:\n" + current + "\n")
    print(outcomes.zipWithIndex.map { case (f, s) => (s, current.getChanges(f)) }.mkString("Выбери следующие состояние:\n", "\n", "\n"))
    readInt(outcomes.length)
  }

}

object ConsolePlayer {
  def readInt(len: Int): Int = {
    try {
      print("--->")
      if (len > 1) {
        val index = StdIn.readInt()
        if (index < 0 || index >= len) {
          println("Error! ")
          readInt(len)
        } else index
      }
      else {
        println(0)
        0
      }
    } catch {
      case e: Exception =>
        println("Error! " + e.getMessage)
        readInt(len)

    }
  }
}
