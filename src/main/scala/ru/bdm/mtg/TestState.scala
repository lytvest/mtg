package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.{LandDropLesson, SkipTurnLesson}
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.teach.{NeuronAgent, SAgent}
import ru.bdm.neurons.{Func, Layer, NeuronModel, NeuronSystem}
import ru.bdm.neurons.gen.GeneticAlgorithmForNeuron


object TestState extends App {

//  println((ManaPool("WWUUBBRR") getCombinations 2) mkString("\n"))
//  val state = State(manaPool = AllSet.empty[Char] ++~ "CU")
//  println(AddDifferentColors("WWUUBBRR", 2) * RemoveMana("CU") act(state) mkString("\n"))



  val ns = NeuronSystem(Layer(94) * Layer(20) * Layer(10) * Layer(1))
  val agent = new SAgent(ns) with SkipTurnLesson

  var i = 0
  while (!Thread.interrupted()) {
    i += 1
    var s_err = 0.0
    var s_score = 0.0
    for (i <- 1 to 100) {
      val game = new Battle(DeckShuffler.allCard.getSeq, agent)
      game.run()
      s_err += agent.alg.error
      s_score += agent.allScore
    }
    println(s"${i}:    err=${s_err / 100}    score=${s_score / 100}")
    ns.saveToFile("super_ns.json")
  }
//
//  for(i <- 1 to 3) {
//    val lesson = new SkipTurnLesson()
//    val game = new Battle(DeckShuffler.allCard.getSeq, new NeuronAgent(nN, true), lesson)
//    game.run()
//    println(s"Res lesson value ${lesson.lessonEvaluation}")
//  }
//
//  nN.saveToFile("SkipTurnReinfTry2.json")


}

