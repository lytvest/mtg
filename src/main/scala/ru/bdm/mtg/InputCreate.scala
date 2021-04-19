package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards._
import ru.bdm.mtg.lands._

object InputCreate {


  val mana = "WURBC"

  val landsActive: Seq[Permanent] = Seq(
    CrumblingVestige(true),
    Mountain(true),
    PeatBog(active = true)
  ) ++ (for {
    color <- "RWUB"
    rest = "RWUB".filter(_ != color)
    choose <- rest.map(Some(_)) :+ None
  } yield Thriving(color, rest, active = true, choose))

  val landsPassive: Seq[Permanent] = landsActive.map(_.copy(false))

  val battlefieldActive: Seq[Permanent] = Seq(
    HandOfEmrakul(true),
    InsolentNeonate(true),
    LotusPetal(),
    MerchantOfTheVale(active = true),
    MerchantOfTheVale(exile = true, active = true),
    UlamogsCrusher(true)
  )
  val battlefieldPassive: Seq[Permanent] = battlefieldActive.map(_.copy(false))

  val hand: Seq[Card] = Seq(
    BreathOfLife(),
    CarefulStudy(),
    CatharticReunion(),
    DangerousWager(),
    DarkRitual(),
    DeepAnalysis(),
    Duress(),
    Exhume(),
    FaithlessLooting(),
    HandOfEmrakul(),
    IdeasUnbound(),
    InsolentNeonate(),
    LotusPetal(),
    Manamorphose(),
    MerchantOfTheVale(),
    Ponder(),
    RiseAgain(),
    SimianSpiritGuide(),
    ThrillOfPossibility(),
    TolarianWinds(),
    UlamogsCrusher(),

    CrumblingVestige(),
    Mountain(),
    PeatBog(),
    SandstoneNeedle(),
    ThrivingBluff(),
    ThrivingIsle(),
    ThrivingMoor()
  )

  val graveyard:Seq[Card] = Seq(
    DeepAnalysis(),
    DragonBreath(),
    FaithlessLooting(),
    HandOfEmrakul(),
    InsolentNeonate(),
    UlamogsCrusher()
  )

  val lands: Seq[Permanent] = landsActive ++ landsPassive
  val battlefield: Seq[Permanent] = battlefieldActive ++ battlefieldPassive



  def apply(state: State): Seq[Double] = {

    (mana.map(count => state.manaPool.getOrElse(count, 0) / 10d) ++
      lands.map(land => state.lands.getOrElse(land, 0) / 4d) ++
      battlefield.map(per => state.battlefield.getOrElse(per, 0) / 4d) ++
      hand.map(card => state.hand.getOrElse(card, 0) / 4d) ++
      graveyard.map(card => state.graveyard.getOrElse(card, 0) / 4d) ++
      Seq(state.discard / 7d, state.endTurnDiscards / 3d, state.draw / 7d, state.numberTurn / 10d, if (state.playedLand)  1d else 0d)
      ).map(_ - 0.5)
  }

  def apply(oldState:State, state:State): Seq[Double] = {
    apply(oldState) ++ apply(state)
  }

  def plusScore(old: State, state:State): Seq[Double] = {
    apply(old, state) :+ (state.score / 1000000)
  }

}
