package com.bazbatlabs.bricksmash.models

import com.bazbatlabs.bricksmash.lib._

class World(val events: WorldEvents) {

  val InitialChances = 30
  val BricksPerRow = 14

  val BrickWidth = World.Width / BricksPerRow
  val BrickHeight = BrickWidth / 2f

  val origin = Vec2.Zero
  val size = Vec2(World.Width, World.Height)

  val bounds = Rect(origin, size)

  var chancesRemaining = InitialChances

  val paddle = new Paddle(bounds, World.Height / 4f, events)
  val ball = new Ball(paddle.center, this)

  var state = State.Normal

  val walls = List(new Wall(Rect(origin, Vec2(0f, size.y)), events),
                   new Wall(Rect(Vec2(origin.x, origin.y + size.y),
                                 Vec2(size.x, 0f)), events),
                   new Wall(Rect(Vec2(origin.x + size.x, origin.y),
                                 Vec2(0f, size.y)), events))

  val brickSize = Vec2(BrickWidth, BrickHeight)

  var bricks = List[Brick]()

  for (i <- 0 until BricksPerRow) {
    val pos = Vec2(origin.x + (i * BrickWidth), World.Height * 3f / 4f)
    bricks = new Brick(Rect(pos, brickSize), Brick.Type.Tough, events) :: bricks
  }

  def floor = bounds.bottom

  def update() {
    if (state == State.Normal) {
      paddle.update()
      ball.update()

      for (brick <- bricks) {
        brick.update()
      }

      clearDestroyedBricks

      if (bricks.isEmpty) {
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
    bricks = bricks.filterNot { brick => brick.isDestroyed }
  }

  def collidables = paddle :: walls ::: bricks

  def startAcceleratingPaddle(dir: Direction.Value) {
    paddle.startAccelerating(dir)
  }

  def stopAcceleratingPaddle(dir: Direction.Value) {
    paddle.stopAccelerating(dir)
  }

  def kickstartBall() {
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
  val Width = 224.0f
  val Height = 186.0f
}
