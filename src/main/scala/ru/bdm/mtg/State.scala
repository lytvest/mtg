package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps


case class State(
                  manaPool: ManaPool.Type = AllSet.empty[Char],
                  hand: AllSet.Type[Card] = AllSet.empty[Card],
                  graveyard: AllSet.Type[Card] = AllSet.empty[Card],
                  battlefield: AllSet.Type[Card] = AllSet.empty[Card],
                  lands: AllSet.Type[Land] = AllSet.empty[Land],
                  library: Seq[Card] = Seq.empty[Card]
                ){
  override def toString: String =
    s"   mana{${manaPool.mkString(", ")}}" +
      s"   hand{${hand.getSeq.mkString(", ")}}" +
      s"   lands{${lands.getSeq.mkString(", ")}}" +
      s"   graveyard{${graveyard.getSeq.mkString(", ")}}" +
      s"   battlefield{${battlefield.getSeq.mkString(", ")}}" +
      s"   library{${library.size}}"
}


