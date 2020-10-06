package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.teach.NeuronAgent

object TestState extends App {

//  println((ManaPool("WWUUBBRR") getCombinations 2) mkString("\n"))
//  val state = State(manaPool = AllSet.empty[Char] ++~ "CU")
//  println(AddDifferentColors("WWUUBBRR", 2) * RemoveMana("CU") act(state) mkString("\n"))


  //val game = new Battle(DeckShuffler.allCard.getSeq, new ConsolePlayer, LessonEmpty)
  var max = 0d
  var iter = 0
  while (true) {
    iter += 1
    val agent = new NeuronAgent(log = false, teacher = true, "saves/ns/n2.json")
   // val agent = new NeuronAgent(log = false, teacher = true)
    val game = new Battle(DeckShuffler.allCard.getSeq, agent)
    game.run()
    // game.save()
    agent.save("saves/ns/n2.json")
    val cur = agent.countRight.toDouble / (agent.countRight + agent.countNoRight)
    if (cur > max){
      max = cur
      agent.save("saves/ns/best.json")
    }
    println(s"$iter) -> rights = ${agent.countRight} no rights = ${agent.countNoRight} ${(cur * 100).toInt}% max = ${(max * 10000).toInt / 100d}%")
  }

}
