package ru.bdm.mtg

import java.io.{File, PrintWriter}
import java.nio.charset.Charset
import java.nio.file.Files

import org.json4s._
import org.json4s.native.Serialization
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, NextTurn}
import ru.bdm.mtg.cards.{BreathOfLife, CarefulStudy, CatharticReunion, DangerousWager, DarkRitual, DeepAnalysis, Duress, Exhume, FaithlessLooting, HandOfEmrakul, IdeasUnbound, InsolentNeonate, LotusPetal, Manamorphose, MerchantOfTheVale, Ponder, RiseAgain, SimianSpiritGuide, ThrillOfPossibility, TolarianWinds, UlamogsCrusher}
import ru.bdm.mtg.lands.{CrumblingVestige, Mountain, PeatBog, SandstoneNeedle, Thriving}

class Battle(val deck: Seq[Card], player: Agent, lesson:Lesson = LessonEmpty, seed: Long = System.currentTimeMillis()) {
  private val shuffler = new DeckShuffler(seed)

  var currentState: State = State(library = shuffler.shuffle(deck))

  def mall(): Unit = {
    currentState = currentState.copy(takeCards = 7)

  }



  def run(): Unit = {
    mall()
    while (lesson.isEnd(currentState)) {
      if (currentState.phase == Phase.takeFirst)
        takeAll()
      if (currentState.phase == Phase.discardFirst)
        discardAll()
      takeAll()
      discardAll()
      currentState = currentState.copy(discard = 0, takeCards = 0)

      shuffleLibrary()

      setPhase(Phase.play)
      val actives = getActiveActions :+ NextTurn
      choosePlayerState(actives)
    }
  }

  private def shuffleLibrary(): Unit = {
    if (currentState.shuffle)
      currentState = currentState.copy(
        library = shuffler.shuffle(currentState.library ++ currentState.верхКолоды),
        верхКолоды = Seq.empty[Card],
        shuffle = false
      )
  }

  private def discardAll(): Unit = {
    while (currentState.discard > 0 && currentState.hand.nonEmpty) {
      setPhase(Phase.discard)
      val actives = getActiveActions
      choosePlayerState(actives)
    }
  }

  private def takeAll(): Unit = {
    while (currentState.takeCards > 0) {
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
    currentState = currentState.copy(phase = phase)
  }

  private def choosePlayerState(actives: Seq[Action]): Unit = {
    val choose = actives.flatMap(_.act(currentState)).distinct
    if (choose.nonEmpty) {
      val index = player.chooseStateServer(currentState, choose)
      val oldState = currentState
      currentState = choose(index)
      lesson.evaluate(oldState, currentState)
    }
  }

  private def takeCards(): Unit = {
    val lib = currentState.верхКолоды ++ currentState.library
    currentState = currentState.copy(
      hand = currentState.hand ++~ lib.slice(0, currentState.takeCards),
      верхКолоды = currentState.верхКолоды.drop(currentState.takeCards),
      library = currentState.library.drop(Math.max(currentState.takeCards - currentState.верхКолоды.size, 0)),
      takeCards = 0)
  }

  private def getActiveActions: Seq[Action] = {
    allCards.flatMap(_.description.filter { case (condition, action) =>
      condition.check(currentState)
    }).map(_._2).toSeq
  }

  private def allCards: Iterable[Card] =
    currentState.hand.keys ++ currentState.lands.keys ++ currentState.graveyard.keys ++ currentState.battlefield.keys


  def save(fileName: String = ""): Unit = {
    implicit val formats = Serialization.formats(Battle.formats)
    val files = new File("").listFiles
    val count = if(files != null) files.count(_.getName.endsWith(".js")) + 1 else 1
    val name = if (fileName.isEmpty) player.name + "_" + count + ".js" else fileName
    Serialization.write(BattleWrite(seed, deck, player.list), new PrintWriter(name,  Charset.forName("UTF-8"))).close()
  }

}

object Battle {
  val formats = new ShortTypeHints(List(

    classOf[CrumblingVestige],
    classOf[Mountain],
    classOf[PeatBog],
    classOf[SandstoneNeedle],
    classOf[Thriving],

    classOf[BreathOfLife],
    classOf[CarefulStudy],
    classOf[CatharticReunion],
    classOf[DangerousWager],
    classOf[DarkRitual],
    classOf[DeepAnalysis],
    classOf[Duress],
    classOf[Exhume],
    classOf[FaithlessLooting],
    classOf[HandOfEmrakul],
    classOf[IdeasUnbound],
    classOf[InsolentNeonate],
    classOf[LotusPetal],
    classOf[Manamorphose],
    classOf[MerchantOfTheVale],
    classOf[Ponder],
    classOf[RiseAgain],
    classOf[SimianSpiritGuide],
    classOf[ThrillOfPossibility],
    classOf[TolarianWinds],
    classOf[UlamogsCrusher]
  )) {
    override val typeHintFieldName: String = "name"
  }
}
