package com.bazbatlabs.bricksmash.lib

class Vec2(val x: Float, val y: Float) {

  def add(that: Vec2) = Vec2(this.x + that.x, this.y + that.y)
  def subtract(that: Vec2) = Vec2(this.x - that.x, this.y - that.y)

  def scale(factor: Float) = Vec2(this.x * factor, this.y * factor)

  def rotate(radians: Float) = {
    val cos = Math.cos(radians).asInstanceOf[Float]
    val sin = Math.sin(radians).asInstanceOf[Float]

    Vec2(this.x * cos - this.y * sin, this.x * sin + this.y * cos)
  }

  def isWithinRect(origin: Vec2, size: Vec2) =
    this.x >= origin.x && this.x <= origin.x + size.x &&
    this.y >= origin.y && this.y <= origin.y + size.y


  override def equals(other: Any): Boolean =
    other match {
      case that: Vec2 =>
        (that canEqual this) && floatEquals(x, that.x) && floatEquals(y, that.y)
      case _ => false
    }

  def canEqual(other: Any): Boolean =
    other.isInstanceOf[Boolean]

  override def hashCode: Int = {
    var result = 17
    result = 31 * result + java.lang.Float.floatToIntBits(x)
    31 * result + java.lang.Float.floatToIntBits(y)
  }

  override def toString = f"(x: $x%.4f, y: $y%.4f)"

  private def floatEquals(a: Float, b: Float) = Math.abs(a - b) < 0.000001
}

object Vec2 {
  def apply(x: Float, y: Float) = new Vec2(x, y)

  def fromAngle(angle: Float) =
    Vec2(Math.cos(angle).asInstanceOf[Float],
         Math.sin(angle).asInstanceOf[Float])

  val Zero = Vec2(0.0f, 0.0f)
}
