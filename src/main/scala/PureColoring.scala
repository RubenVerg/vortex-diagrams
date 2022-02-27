package com.rubenverg.vortex

class PureColoring(description: String)(f: (Int, Int, Int, Int, Operation) => String) extends Coloring(description):
  def apply(a: Int, b: Int, mod: Int, op: Int, operation: Operation) = f(a, b, mod, op, operation)
  def reset(): Unit = ()