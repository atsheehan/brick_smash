package com.bazbatlabs.bricksmash.lib

class Rect(val origin: Vec2, val size: Vec2) {

  def left = origin.x
  def right = origin.x + size.x
  def bottom = origin.y
  def top = origin.y + size.y

  override def equals(other: Any): Boolean =
    other match {
      case that: Rect =>
        (that canEqual this) && origin == that.origin && size == that.size

      case _ => false
    }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Rect]

  override def hashCode: Int = (31 * (31 + origin.hashCode)) + size.hashCode

  override def toString = f"x: ${origin.x}%.4f, y: ${origin.y}%.4f, " +
    f"w: ${size.x}%.4f, h: ${size.y}%.4f"
}

object Rect {
  def apply(origin: Vec2, size: Vec2) = new Rect(origin, size)
}
