package com.bazbatlabs.bricksmash.models

class Paddle(val fieldBounds: Rect, startY: Float, val events: WorldEvents) extends Collidable {

  val size = Vec2(Paddle.Width, Paddle.Height)

  private var acceleratingLeft = false
  private var acceleratingRight = false

  val startX = fieldBounds.left + (fieldBounds.right - fieldBounds.left - size.x) / 2f

  var pos = Vec2(startX, startY)
  var vel = Vec2.Zero

  def bounds = Rect(pos, size)

  def update() {
    var accelX = 0f

    if (acceleratingLeft) { accelX -= Paddle.Acceleration }
    if (acceleratingRight) { accelX += Paddle.Acceleration }

    val accel = Vec2(accelX, 0f)

    vel = vel.add(accel).scale(1f - Paddle.Friction)

    if (vel.x > Paddle.MaxSpeed) { vel = Vec2(Paddle.MaxSpeed, 0f) }
    if (vel.x < -Paddle.MaxSpeed) { vel = new Vec2(-Paddle.MaxSpeed, 0f) }

    pos = pos.add(vel)

    if (pos.x + size.x > fieldBounds.right) {
      vel = Vec2.Zero
      pos = Vec2(fieldBounds.right - size.x, pos.y)
    }

    if (pos.x < fieldBounds.left) {
      vel = Vec2.Zero
      pos = Vec2(fieldBounds.left, pos.y)
    }
  }

  def startAccelerating(direction: Direction) {
    toggleAcceleration(direction, true)
  }

  def stopAccelerating(direction: Direction) {
    toggleAcceleration(direction, false)
  }

  def center = Vec2(pos.x + (size.x / 2f), pos.y + size.y)

  def hit {
    events.enqueue(Event.PaddleHit)
  }

  def deflect(vel: Vec2, collision: Vec2, axis: Axis) =
    axis match {
      case Axis.X => Vec2(-vel.x, vel.y)
      case Axis.Y => {
        val rotation = 0.5f - (collision.x - pos.x) / size.x
        Vec2(vel.x, -vel.y).rotate(rotation)
      }
      case _ => Vec2.Zero
    }

  def toggleAcceleration(direction: Direction, start: Boolean) {
    direction match {
      case Direction.LEFT => acceleratingLeft = start
      case Direction.RIGHT => acceleratingRight = start
    }
  }

  def isActive = true
}

object Paddle {
    val Friction = 0.1f
    val Acceleration = 0.75f
    val Width = 50f
    val Height = 10f
    val MaxSpeed = 5f
}
