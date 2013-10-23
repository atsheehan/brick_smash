package com.bazbatlabs.bricksmash.models

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class World(val events: WorldEvents) {

  val origin = Vec2.ZERO
  val size = Vec2(World.Width, World.Height)

  val bounds = Rect(origin, size)

  var chancesRemaining = World.InitialChances

  val paddle = new Paddle(bounds, 50.0f, events)
  val ball = new Ball(paddle.center, this)

  var state = State.Normal

  val walls = List(new Wall(Rect(origin, Vec2(0f, size.y)), events),
                   new Wall(Rect(Vec2(origin.x, origin.y + size.y),
                                 Vec2(size.x, 0f)), events),
                   new Wall(Rect(Vec2(origin.x + size.x, origin.y),
                                 Vec2(0f, size.y)), events))

  val brickSize = Vec2(World.BrickWidth, World.BrickHeight)

  private var _bricks = List[Brick]()

  for (i <- 0 until World.BricksPerRow) {
    val pos = Vec2(origin.x + (i * World.BrickWidth), 250f)
    _bricks = new Brick(Rect(pos, brickSize), Brick.Type.TOUGH, events) :: _bricks
  }

  def bricks: java.util.List[Brick] = ListBuffer(_bricks: _*)

  def floor = bounds.bottom

  def update {
    if (state == State.Normal) {
      paddle.update()
      ball.update()

      for (brick <- _bricks) {
        brick.update()
      }

      clearDestroyedBricks

      if (_bricks.isEmpty) {
        state = State.Cleared
      }

      if (ball.isLost) {
        chancesRemaining -= 1

        if (chancesRemaining > 0) {
          ball.reset(paddle.center)
        } else {
          state = State.GameOver
        }
      }
    }
  }

  def clearDestroyedBricks {
    _bricks = _bricks.filterNot { brick => brick.isDestroyed }
  }

  def collidables: java.util.List[Collidable] = ListBuffer(paddle :: walls ::: _bricks : _ *)

  def startAcceleratingPaddle(dir: Direction) {
    paddle.startAccelerating(dir)
  }

  def stopAcceleratingPaddle(dir: Direction) {
    paddle.stopAccelerating(dir)
  }

  def kickstartBall {
    val pi = Math.PI.asInstanceOf[Float]
    val angle = if (paddle.vel.x > 0f) pi / 4f
                else if (paddle.vel.x < 0f) 3f * pi / 4f
                else pi / 2f

    ball.kickstart(angle)
  }

  def isGameOver = state == State.GameOver
  def isCleared = state == State.Cleared


  object State extends Enumeration {
    type State = Value
    val Normal = Value("Normal")
    val Cleared = Value("Cleared")
    val GameOver = Value("GameOver")
  }
}

object World {
  val Width = 400.0f
  val Height = 300.0f

  val InitialChances = 30
  val BricksPerRow = 16

  val BrickWidth = Width / BricksPerRow
  val BrickHeight = BrickWidth / 3.0f
}
