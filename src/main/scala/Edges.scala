package com.rubenverg.vortex

import scala.collection.mutable as M

object Edges extends Predef:
  @scala.scalajs.js.annotation.JSExportTopLevel("allEdges", "com.rubenverg.vortex.debug")
  def all(m: Int, k: Int): Seq[(Int, Int)] = (0 until m).map(x => x -> from(m, k)(x))
  @scala.scalajs.js.annotation.JSExportTopLevel("edgesInto", "com.rubenverg.vortex.debug")
  def into(m: Int, k: Int)(v: Int): Seq[Int] = (0 until k).map(s => (v + m * s) / k.toDouble).filter(_.isInt).map(_.toInt)
  @scala.scalajs.js.annotation.JSExportTopLevel("edgeFrom", "com.rubenverg.vortex.debug")
  def from(m: Int, k: Int)(v: Int): Int = (v * k) % m
