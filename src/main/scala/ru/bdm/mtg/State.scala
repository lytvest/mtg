package ru.bdm.mtg


case class State(
             graveyard: AllSet.Type[Card] = AllSet.empty[Card],
             hand: AllSet.Type[Card] = AllSet.empty[Card],
             battlefield: AllSet.Type[Card] = AllSet.empty[Card],
             library: AllSet.Type[Card] = AllSet.empty[Card],
             lands: AllSet.Type[Card] = AllSet.empty[Card],
             manaPool: ManaPool.Type = AllSet.empty[Char],
             discard: Int = 0
           ) {


}


