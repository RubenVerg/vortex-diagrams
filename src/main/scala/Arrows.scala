package com.rubenverg.vortex

import scala.math.*
import scala.collection.mutable as M
import org.scalajs.dom.CanvasRenderingContext2D

object Arrows:
  extension (ctx: CanvasRenderingContext2D)
    def arrow(a: Point, b: Point, control: Seq[Point] = Seq(Point(0, 0.5), Point(-5, 0.5), Point(-5, 3))): Unit =
      val d = b - a
      val len = d.magnitude
      val Point(c, s) = d.normalize
      val points =
        control.map(point => Point(if point.x < 0 then point.x + len else point.x, point.y))
          ++: Point(len, 0)
          +: control.reverse.map(point => Point(if point.x < 0 then point.x + len else point.x, -point.y))
          ++: Point.Origin +: Seq.empty[Point]
      for (point, i) <- points.zipWithIndex do
        val x = point.x * c - point.y * s + a.x
        val y = point.x * s + point.y * c + a.y
        if i == 0 then ctx.moveTo(x, y)
        else ctx.lineTo(x, y)
