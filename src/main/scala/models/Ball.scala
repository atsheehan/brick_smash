package com.bazbatlabs.bricksmash.models

import com.bazbatlabs.bricksmash.lib._

class Ball(initialCenter: Vec2, val world: World) {

  var center = Vec2(initialCenter.x, initialCenter.y + Ball.Radius)
  var vel = Vec2.Zero

  val size = Vec2(Ball.Diameter, Ball.Diameter)
  val halfSize = Vec2(Ball.Radius, Ball.Radius)

  var state = State.Stuck

  def bounds = Rect(pos, size)

  def reset(center: Vec2) {
    this.center = Vec2(center.x, center.y + Ball.Radius)
    state = State.Stuck
  }

  def pos = center.subtract(halfSize)
  def isLost = state == State.Lost

  def kickstart(angle: Float) {
    if (state == State.Stuck) {
      vel = Vec2.fromAngle(angle).scale(Ball.Speed)
      state = State.Moving
    }
  }

  def update() {
    state match {
      case State.Stuck => {
        val paddleCenter = world.paddle.center
        center = Vec2(paddleCenter.x, paddleCenter.y + Ball.Radius)
      }
      case State.Moving => {
        move(vel)

        if (center.y - Ball.Radius < world.floor) {
          state = State.Lost
        }
      }
    }
  }

  def move(effectiveVel: Vec2) {
    val newCenter = center.add(effectiveVel)

    // Find the closest collision amongst all of the collidable objects
    var collision = Collision.None

    for (collidable <- world.collidables) {
      if (collidable.isActive) {
        collision = nearestCollision(collidable, effectiveVel, newCenter, collision)
      }
    }

    if (collision.distance <= 1f) {
      var actualVel = effectiveVel.scale(collision.distance)
      var remainingVel = effectiveVel.subtract(actualVel)

      center = center.add(actualVel)

      // Reverse direction after a collision
      vel = collision.collidable.deflect(vel, center, collision.axis)
      remainingVel = collision.collidable.deflect(remainingVel, center, collision.axis)

      collision.collidable.hit()
      move(remainingVel)

    } else {
      center = newCenter
    }
  }

  def nearestCollision(collidable: Collidable, effectiveVel: Vec2,
                       newCenter: Vec2, best: Collision) = {

    var bestCollision = best

    val bounds = collidable.bounds

    val top = bounds.top + Ball.Radius
    val bottom = bounds.bottom - Ball.Radius
    val left = bounds.left - Ball.Radius
    val right = bounds.right + Ball.Radius

    if (effectiveVel.y < 0f) {
      if (center.y >= top && newCenter.y <= top) {
        val distance = (top - center.y) / (newCenter.y - center.y)

        if (distance < bestCollision.distance) {
          val xIntercept = center.x + (effectiveVel.x * distance)

          if (xIntercept > left && xIntercept < right) {
            bestCollision = Collision(collidable, distance, Axis.Y)
          }
        }
      }
    }

    if (effectiveVel.y > 0f) {
      if (center.y <= bottom && newCenter.y >= bottom) {
        val distance = (bottom - center.y) / (newCenter.y - center.y)

        if (distance < bestCollision.distance) {
          val xIntercept = center.x + (effectiveVel.x * distance)

          if (xIntercept > left && xIntercept < right) {
            bestCollision = Collision(collidable, distance, Axis.Y)
          }
        }
      }
    }

    if (effectiveVel.x < 0.0f) {
      if (center.x >= right && newCenter.x <= right) {
        val distance = (right - center.x) / (newCenter.x - center.x)

        if (distance < bestCollision.distance) {
          val yIntercept = center.y + (effectiveVel.y * distance)

          if (yIntercept > bottom && yIntercept < top) {
            bestCollision = Collision(collidable, distance, Axis.X)
          }
        }
      }
    }

    if (effectiveVel.x > 0.0f) {
      if (center.x <= left && newCenter.x >= left) {
        val distance = (left - center.x) / (newCenter.x - center.x)

        if (distance < bestCollision.distance) {
          val yIntercept = center.y + (effectiveVel.y * distance)

          if (yIntercept > bottom && yIntercept < top) {
            bestCollision = Collision(collidable, distance, Axis.X)
          }
        }
      }
    }

    bestCollision
  }

  class Collision(val collidable: Collidable, val distance: Float, val axis: Axis.Value)

  object Collision {
    def apply(collidable: Collidable, distance: Float, axis: Axis.Value) =
      new Collision(collidable, distance, axis)

    def None = Collision(null, Float.MaxValue, Axis.X)
  }

  object State extends Enumeration {
    type State = Value
    val Stuck = Value("Stuck")
    val Moving = Value("Moving")
    val Lost = Value("Lost")
  }
}

object Ball {
  val Radius = 5f
  val Diameter = Radius + Radius
  val Speed = 5f
}
