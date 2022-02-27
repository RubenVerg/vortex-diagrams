package com.rubenverg.vortex

import Predef.*

class AdditionSpec extends Spec:
  describe("The AdditionOperation") {
    it("coherently performs additions") {
      forAll { (modulo: Int, operator: Int, n: Int) =>
        whenever(modulo > 0) {
          AdditionOperation(modulo)(operator)(n) should === ((n + operator) %% modulo)
        }
      }
    }

    it("performs like pure addition with small operators and numbers") {
      forAll { (modulo: Short, operator: Byte, n: Byte) =>
        val op = operator.toInt
        val num = n.toInt
        whenever(modulo > 0 && op + num < modulo && op + num >= 0) {
          AdditionOperation(modulo | 0)(op)(num) should === (num + op)
        }
      }
    }

    it("correctly identifies edges") {
      forAll { (modulo: Int, op: Int) =>
        whenever(modulo > 0) {
          AdditionOperation.edges(modulo)(op).foreach { case (from, to) =>
            ((to - from) %% modulo) should === (op %% modulo)
          }
        }
      }
    }
  }
