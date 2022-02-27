package com.rubenverg.vortex

import scala.collection.mutable as M

import Predef.*

object LoopColoring extends Coloring(
  """Lines that are part of a loop get a color per loop, the ones that point into a loop node but aren't in a loop themselves are black"""
):
  def apply(a: Int, b: Int, mod: Int, op: Int, operation: Operation) =
    val (alone, loops) = LoopColoring.loops(mod, op, operation)
    if alone.contains(a) then "black"
    else s"hsl(${loops.indexWhere(_.contains(a)).toDouble / loops.length}turn, 100%, 50%)"

  def reset(): Unit = ()

  private val knownLoops = M.Map.empty[(Int, Int, Operation), (Seq[Int], Seq[Seq[Int]])]

  @scala.scalajs.js.annotation.JSExportTopLevel("loops", "com.rubenverg.vortex.debug")
  def loops(mod: Int, op: Int, operation: Operation): (Seq[Int], Seq[Seq[Int]]) =
    knownLoops.getOrElseUpdate((mod, op, operation), ({() =>
      debug(s"Computing loops for $operation $op mod $mod")
      val dropped = M.ArrayBuffer.empty[Int]
      val potential = M.ArrayBuffer.range(0, mod)
      def findEdgesInto(n: Int) = operation.edgesInto(mod)(op)(n).filterNot(dropped.contains).map(_ -> n)
      def findEdgesFrom(n: Int) = operation.edgesFrom(mod)(op)(n).map(n -> _)
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
      val edges = potential.flatMap(findEdgesFrom)
      debug(s"Edges: $edges for $potential")
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

