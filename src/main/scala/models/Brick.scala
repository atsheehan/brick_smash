package com.bazbatlabs.bricksmash.models

class Brick(val bounds: Rect, val brickType: Brick.Type.Value, val events: WorldEvents) extends Collidable {

  val BreakingDuration = 19

  val foo = Brick.Type.Normal

  var state = Brick.State.Normal
  var stateCounter = 0

  var durability = if (brickType == Brick.Type.Tough) 2 else 1

  def isActive = state == Brick.State.Normal
  def isDestroyed = state == Brick.State.Destroyed
  def isBreaking = state == Brick.State.Breaking

  def hit() {
    events.enqueue(Event.BrickHit)

    if (state == Brick.State.Normal) {
      durability -= 1

      if (durability <= 0) {
        state = Brick.State.Breaking
      }
    }
  }

  def update() {
    if (state == Brick.State.Breaking) {
      stateCounter += 1

      if (stateCounter > BreakingDuration) {
        state = Brick.State.Destroyed
      }
    }
  }

  def deflect(vel: Vec2, collision: Vec2, axis: Axis.Value) =
    if (axis == Axis.X) Vec2(-vel.x, vel.y) else Vec2(vel.x, -vel.y)
}

object Brick {
  object Type extends Enumeration {
    type Type = Value
    val Normal = Value("Tough")
    val Tough = Value("Tough")
  }


  object State extends Enumeration {
    type State = Value
    val Normal = Value("Normal")
    val Breaking = Value("Breaking")
    val Destroyed = Value("Destroyed")
  }
}
