package com.rubenverg.vortex

import scala.collection.mutable as M

object LoopAlgorithm extends Algorithm, Predef:
  def apply(a: Int, b: Int, mod: Int, mult: Int) =
    val (alone, loops) = LoopAlgorithm.loops(mod, mult)
    if alone.contains(a) then "black"
    else s"hsl(${loops.indexWhere(_.contains(a)).toDouble / loops.length}turn, 100%, 50%)"

  def reset(): Unit = ()

  private val knownLoops = M.Map.empty[(Int, Int), (Seq[Int], Seq[Seq[Int]])]

  @scala.scalajs.js.annotation.JSExportTopLevel("loops", "com.rubenverg.vortex.debug")
  def loops(mod: Int, mult: Int): (Seq[Int], Seq[Seq[Int]]) =
    knownLoops.getOrElseUpdate((mod, mult), ({() =>
      debug(s"Computing loops for times $mult mod $mod")
      val dropped = M.ArrayBuffer.empty[Int]
      val potential = M.ArrayBuffer.range(0, mod)
      def findEdgesInto(n: Int) = Edges.into(mod, mult)(n).filter(!dropped.contains(_))
      def findEdgeFrom(n: Int) = Edges.from(mod, mult)(n)
      debug("Looking for heads")
      debug(s"Found ${potential.count(findEdgesInto(_).isEmpty)} heads")
      while potential.exists(findEdgesInto(_).isEmpty) do
        debug(s"Found ${potential.count(findEdgesInto(_).isEmpty)} heads")
        val values = potential.filter(findEdgesInto(_).isEmpty)
        potential --= values
        dropped ++= values
        debug(s"Removed $values")
      val loops = M.ArrayBuffer.empty[Seq[Int]]
      debug("Computing loops")
      val edges = potential.map(x => x -> findEdgeFrom(x))
      for
        (start, next) <- edges
      do
        val loop = M.ArrayBuffer(start)
        var n = next
        while !loop.contains(n) do
          loop += n
          n = edges.find(_._1 == n).get._2
        loops += loop.toSeq
      (dropped.toSeq, loops.toSeq)
    })())

