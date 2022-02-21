package com.rubenverg.vortex

import scala.math.*

case class Point(x: Double, y: Double):
  inline def +(that: Point) = Point(this.x + that.x, this.y + that.y)
  inline def -(that: Point) = Point(this.x - that.x, this.y - that.y)
  inline def unary_- = Point(-x, -y)
  inline def *(factor: Double) = Point(x * factor, y * factor)
  inline def /(factor: Double) = Point(x / factor, y / factor)
  inline def distance(that: Point) = sqrt(pow(this.x - that.x, 2) + pow(this.y - that.y, 2))
  inline def magnitude = distance(Point.Origin)
  inline def normalize = this / magnitude
  inline def angle = atan2(y, x)
  inline def slope(that: Point) = (that.y - this.y) / (that.x - this.x)

  inline def rotate(by: Double) = Point.angleMagnitude(angle + by, magnitude)

  inline def toInts = (x.toInt, y.toInt)

  override def toString = s"($x, $y)"

object Point:
  val Origin = Point(0, 0)

  def unitCircle(angle: Double) = Point(cos(angle), sin(angle))
  def angleMagnitude(angle: Double, magnitude: Double) = unitCircle(angle) * magnitude