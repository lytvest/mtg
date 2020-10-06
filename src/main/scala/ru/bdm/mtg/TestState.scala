package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.{LandDropLesson, SkipTurnLesson}
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.teach.{EGreedyNeuronAgent, NeuronAgent}
import ru.bdm.neurons.{BackpropagationAlgorithm, Layer, NeuronSystem}

import scala.io.StdIn.readInt
import scala.util.Random

object TestState extends App {

//  println((ManaPool("WWUUBBRR") getCombinations 2) mkString("\n"))
//  val state = State(manaPool = AllSet.empty[Char] ++~ "CU")
//  println(AddDifferentColors("WWUUBBRR", 2) * RemoveMana("CU") act(state) mkString("\n"))


  //val game = new Battle(DeckShuffler.allCard.getSeq, new ConsolePlayer, LessonEmpty)
  //val game = new Battle(DeckShuffler.allCard.getSeq, new NeuronAgent(log = true))
  //game.run()
  //game.save()

  val rand  = Random
  val layer = Layer(3) * Layer(5) * Layer(10) * Layer(1)
  val ns = NeuronSystem(layer).setRandomWeights()
  val alg = new BackpropagationAlgorithm(ns, 0.5)
  val trainSetSize = 10000000
  val checkSize = 100000
  def check(a: Double, b: Double, c: Double): Double = if(a + b > c && a + c > b && b + c > a) 1 else 0

  for(i <- 1 to trainSetSize){
    val input = Seq(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())
    val answer = check(input.head, input(1), input(2))
    alg.teach(input, Seq(answer))
    if(i % (trainSetSize / 100) == 0) {
      println(i / (trainSetSize / 100))
      alg.speed *= 0.99
    }
  }

  var accuracy = 0.0
  for(i <- 1 to checkSize){
    val input = Seq(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())
    val answer = check(input.head, input(1), input(2))
    val res = ns.work(input)
    accuracy += Math.abs(res.head - answer)
  }

  println(accuracy / checkSize)

  /*val agent = new EGreedyNeuronAgent(log = false)
  val lesson = SkipTurnLesson
  var game = new Battle(DeckShuffler.allCard.getSeq, agent, lesson = lesson)
  val alg = new BackpropagationAlgorithm(agent.ns, 0.1)
  var step = 0
  var finGame = false
  var prevValue = 0
  var avgVal = 0
  val maxStep = 1000000
  while(step < maxStep){
    step += 1
    if(finGame) {
      game = new Battle(DeckShuffler.allCard.getSeq, agent, lesson = lesson)
      //game.mulligan()
    }
    val prevState = game.currentState
    finGame = game.tick()
    val currentValue =  lesson.eval - prevValue
    prevValue = lesson.eval
    avgVal += currentValue
    alg.teach(InputCreate.apply(prevState), Seq(currentValue))
    val percent = (maxStep / 100) * 1.0
    if(step % percent == 0) {
      println(step / percent + "%  Avg val this percent = " + avgVal / percent + " ")
      avgVal = 0
    }

  }*/


}
