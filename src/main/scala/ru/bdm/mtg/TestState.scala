package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.{LandDropLesson, SkipTurnLesson, SuperLesson}
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.teach.{AIAgent, NeuronAgent, SAgent}
import ru.bdm.neurons.{BackpropagationAlgorithm, Func, Layer, NeuronModel, NeuronSystem}
import ru.bdm.neurons.gen.GeneticAlgorithmForNeuron


object TestState extends App {
  println("starting...")

  var epoh = 0
  while (true) {

    val agent = new SAgent
    val battle = new Battle(DeckShuffler.allCard.getSeq,agent, new SuperLesson)
    epoh += 1
    var score = 0.0
    val K = 100
    for (_ <- 1 to K) {
      battle.run()
      score += agent.allScore
    }
    println(s"  $epoh [${epoh*K}] :   score = ${score / K}   e=${agent.e}    ${battle.currentState.battlefield}")
    agent.ns.save("ns.nnet");
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

