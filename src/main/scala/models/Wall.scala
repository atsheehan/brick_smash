package com.bazbatlabs.bricksmash.models

class Wall(val bounds: Rect, private val events: WorldEvents) extends Collidable {

  def hit() {
    events.enqueue(Event.WallHit)
  }

  def deflect(vel: Vec2, collision: Vec2, axis: Axis.Value) = {
    axis match {
      case Axis.X => new Vec2(-vel.x, vel.y)
      case Axis.Y => new Vec2(vel.x, -vel.y)
      case _ => Vec2.Zero
    }
  }

  def isActive = true
}
