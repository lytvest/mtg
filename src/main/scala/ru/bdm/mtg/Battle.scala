package ru.bdm.mtg

import java.io.{File, PrintWriter}
import java.nio.charset.Charset
import java.nio.file.Files


import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}
import ru.bdm.mtg.cards.{BreathOfLife, CarefulStudy, CatharticReunion, DangerousWager, DarkRitual, DeepAnalysis, DragonBreath, Duress, Exhume, FaithlessLooting, HandOfEmrakul, IdeasUnbound, InsolentNeonate, LotusPetal, Manamorphose, MerchantOfTheVale, Ponder, RiseAgain, SimianSpiritGuide, ThrillOfPossibility, TolarianWinds, UlamogsCrusher}
import ru.bdm.mtg.lands.{CrumblingVestige, Mountain, PeatBog, SandstoneNeedle, Thriving}

class Battle(val deck: Seq[Card], agent: Agent, val lesson: Lesson = Lesson.empty , seed: Long = System.currentTimeMillis()) {
  private val shuffler = new DeckShuffler(seed)

  var state: State = State(library = shuffler.shuffle(deck))

  def mulligan(): Unit = {
    state = state.copy(draw = 7)
  }


  def run(): Unit = {
    mulligan()
    while (!lesson.isEnd(state))
      tick()
    agent.endGame()
  }

  def tick(): Unit = {

    if (state.phase == Phase.takeFirst)
      applyDraws()
    if (state.phase == Phase.discardFirst)
      applyDiscards()
    applyDraws()
    applyDiscards()
    state = state.copy(discard = 0, draw = 0)
    applyShuffleLibrary()


    setPhase(Phase.play)
    val actives = getActiveActions :+ NextTurn
    choosePlayerState(actives)

  }

  private def applyShuffleLibrary(): Unit = {
    if (state.shuffle)
      state = state.copy(
        library = shuffler.shuffle(state.library ++ state.topOfLibrary),
        topOfLibrary = Seq.empty[Card],
        shuffle = false
      )
  }

  private def applyDiscards(): Unit = {
    while (state.discard > 0 && state.hand.nonEmpty) {
      setPhase(Phase.discard)
      val actives = getActiveActions
      choosePlayerState(actives)
    }
  }

  private def applyDraws(): Unit = {
    while (state.draw > 0) {
      setPhase(Phase.take)
      val actives = getActiveActions
      if (actives.isEmpty)
        takeCards()
      else {
        choosePlayerState(actives)
      }
    }
  }

  private def setPhase(phase: String): Unit = {
    state = state.copy(phase = phase)
  }

  private def choosePlayerState(actives: Seq[Action]): Unit = {
    val choose = actives.flatMap(_.act(state)).distinct.map(state => state.copy(score = lesson.evaluate(state, state)))
    if (choose.nonEmpty) {
      val index = agent.chooseStateServer(state, choose)
      state = choose(index)
    }
  }

  private def takeCards(): Unit = {
    val lib = state.topOfLibrary ++ state.library
    state = state.copy(
      hand = state.hand ++~ lib.slice(0, state.draw),
      topOfLibrary = state.topOfLibrary.drop(state.draw),
      library = state.library.drop(Math.max(state.draw - state.topOfLibrary.size, 0)),
      draw = 0)
  }

  private def getActiveActions: Seq[Action] = {
    allCards.flatMap(_.description.filter { case (condition, action) =>
      condition.check(state)
    }).map(_._2).toSeq
  }

  private def allCards: Iterable[Card] =
    state.hand.keys ++ state.lands.keys ++ state.graveyard.keys ++ state.battlefield.keys


//  def save(fileName: String = ""): Unit = {
//    implicit val formats = Serialization.formats(Battle.formats)
//    val files = new File("saves/").listFiles
//    val count = if (files != null) files.count(_.getName.endsWith(".json")) + 1 else 1
//    val name = if (fileName.isEmpty) agent.name + "_" + count + ".json" else fileName
//    Serialization.write(BattleWrite(seed, deck, agent.list), new PrintWriter("saves/" + name, Charset.forName("UTF-8"))).close()
//  }

}

object Battle {
//  val formats = new ShortTypeHints(List(
//
//    classOf[CrumblingVestige],
//    classOf[Mountain],
//    classOf[PeatBog],
//    classOf[SandstoneNeedle],
//    classOf[Thriving],
//
//    classOf[BreathOfLife],
//    classOf[CarefulStudy],
//    classOf[CatharticReunion],
//    classOf[DangerousWager],
//    classOf[DarkRitual],
//    classOf[DeepAnalysis],
//    classOf[DragonBreath],
//    classOf[Duress],
//    classOf[Exhume],
//    classOf[FaithlessLooting],
//    classOf[HandOfEmrakul],
//    classOf[IdeasUnbound],
//    classOf[InsolentNeonate],
//    classOf[LotusPetal],
//    classOf[Manamorphose],
//    classOf[MerchantOfTheVale],
//    classOf[Ponder],
//    classOf[RiseAgain],
//    classOf[SimianSpiritGuide],
//    classOf[ThrillOfPossibility],
//    classOf[TolarianWinds],
//    classOf[UlamogsCrusher]
//  )) {
//    override val typeHintFieldName: String = "name"
//  }
}
