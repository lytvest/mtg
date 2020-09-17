package ru.bdm.mtg

class ManaPool(mana: Map[Char, Int]) extends AllSet[Char](mana) {
  def pay(cost:String): List[AllSet[Char]] ={
    var states = List[AllSet[Char]]()

    var pool = this.copy()
    var c = 0
    for(mana <- cost){
      if(mana != 'C') {
        if (!pool.contains(mana)) return List.empty[AllSet[Char]]
        else pool -= mana
      }
      else
        if(!pool.contains(mana)) c += 1
        else pool -= mana
    }

    def getStates(deep:Int, pool:AllSet[Char], prev:Int = 0): List[AllSet[Char]] ={
      if(deep == 0)
        return List(pool)
      var list = List.empty[AllSet[Char]]
      for(((mana, number), index) <- pool.map.zipWithIndex) {
          if(index >= prev) list :::= getStates(deep - 1, pool - mana, index)
      }
      list
    }
    states = getStates(c, pool)
    states
  }

}

//object ManaPool{ def apply(mana:String)=new ManaPool((new AllSet[Char]()++mana).map)}