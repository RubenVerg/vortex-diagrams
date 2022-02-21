package com.rubenverg.vortex

class PureAlgorithm(f: (Int, Int, Int, Int) => String) extends Algorithm:
  def apply(a: Int, b: Int, mod: Int, mult: Int) = f(a, b, mod, mult)
  def reset(): Unit = ()