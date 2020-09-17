package ru.bdm.mtg


object ManaPool {
  import AllSet._

  type ManaPool = Map[Char, Int]
  type Type = ManaPool

  implicit class ManaPoolOps[T](val map: Map[Char, Int]) {

    def payColorMana(cost: String): Option[ManaPool] = {
      Some(cost.foldRight(map){ case (color, pool) =>
        if (pool.contains(color))
          pool -~ color
        else
          return None
      })
    }
    def canPay(cost:String): Boolean = {
      getProperties(cost) match {
        case (leftC, pool) =>
        if(pool.isDefined)
          pool.get.values.sum >= leftC
        else false
      }
    }

    private def getProperties(cost: String): (Int, Option[ManaPool]) = {
      val definedCost = cost.filter(_ != 'C')
      val countC = cost.count(_ == 'C')
      val paidC = Math.min(countC, map.getOrElse('C', 0))
      val pool = payColorMana(definedCost + "C" * paidC)
      (countC - paidC, pool)
    }

    def pay(cost: String): List[ManaPool] = {
      getProperties(cost) match { case (leftC, pool) =>
        if(pool.isDefined) pool.get.getCombinations(leftC)
        else List.empty
      }
    }

    def write: String = map.map{case (ch, count) => ch * count}.mkString("")
  }

  def apply(s: String): ManaPool = new AllSetOps(Map.empty[Char, Int]) ++~ s
}
