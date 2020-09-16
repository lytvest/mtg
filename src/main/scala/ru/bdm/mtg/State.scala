package ru.bdm.mtg


case class State(
             graveyard: AllSet[Card] = AllSet(),
             hand: AllSet[Card] = AllSet(),
             battlefield: AllSet[Card] = AllSet(),
             library: AllSet[Card] = AllSet(),
             lands: AllSet[Card] = AllSet(),
             manaPool: AllSet[Char] = AllSet(),
             discard: Int = 0
           ) {


}


