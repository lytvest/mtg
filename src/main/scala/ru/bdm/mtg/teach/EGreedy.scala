package ru.bdm.mtg.teach

import scala.util.Random

object EGreedy {

  /**
   * @return index choose element
   */
  def apply[T](seq: Seq[T], e: Double = 0.1, rand: Random = Random)(implicit ord: Ordering[T]): Int = {
    val max = seq.zipWithIndex.max
    var chosen = rand.nextInt(seq.size)
    if(rand.nextDouble() > e)
      chosen = max._2
    chosen
  }
}
