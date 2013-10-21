package com.bazbatlabs.bricksmash.models

class Color(val r: Float, val g: Float, val b: Float, val a: Float) {

  override def equals(other: Any): Boolean =
    other match {
      case that: Color =>
        (that canEqual this) &&
        r == that.r && g == that.g &&
        b == that.b && a == that.a
      case _ => false
    }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Color]

  override def hashCode: Int = {
    var result = 17
    result = 31 * result + java.lang.Float.floatToIntBits(r)
    result = 31 * result + java.lang.Float.floatToIntBits(g)
    result = 31 * result + java.lang.Float.floatToIntBits(b)
    31 * result + java.lang.Float.floatToIntBits(a)
  }

  override def toString = f"r: $r%.3f, g: $g%.3f, b: $b%.3f, a: $a%.3f"
}

object Color {
  def apply(r: Float, g: Float, b: Float, a: Float) = new Color(r, g, b, a)

  val RED = Color(1.0f, 0.0f, 0.0f, 1.0f)
  val GREEN = Color(0.0f, 1.0f, 0.0f, 1.0f)
  val BLUE = Color(0.0f, 0.0f, 1.0f, 1.0f)
  val YELLOW = Color(1.0f, 1.0f, 0.0f, 1.0f)
  val WHITE = Color(1.0f, 1.0f, 1.0f, 1.0f)
  val LIGHT_GRAY = Color(0.66f, 0.66f, 0.66f, 1.0f)
  val DARK_GRAY = Color(0.33f, 0.33f, 0.33f, 1.0f)
  val BLACK = Color(0.0f, 0.0f, 0.0f, 1.0f)
  val CLEAR = Color(0.0f, 0.0f, 0.0f, 0.0f)
}
