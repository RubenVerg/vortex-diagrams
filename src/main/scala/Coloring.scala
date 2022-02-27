package com.rubenverg.vortex

trait Coloring(val description: String):
  def apply(a: Int, b: Int, mod: Int, op: Int, operation: Operation): String
  def reset(): Unit
