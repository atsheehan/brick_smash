package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.lib._

class WorldView(val world: World, val events: WorldEvents,
                val images: ImageMap, val sounds: SoundMap,
                val artist: Artist) {

  val BorderWidth = 6f

  val bounds = world.bounds

  val leftBorder = Rect(Vec2(bounds.left - BorderWidth, bounds.bottom),
                        Vec2(BorderWidth, bounds.size.y))

  val leftCap = Rect(Vec2(bounds.left - BorderWidth, bounds.top),
                     Vec2(BorderWidth, BorderWidth))

  val rightBorder = Rect(Vec2(bounds.right, bounds.bottom),
                         Vec2(BorderWidth, bounds.size.y))

  val rightCap = Rect(Vec2(bounds.right, bounds.top),
                      Vec2(BorderWidth, BorderWidth))

  val topBorder = Rect(Vec2(bounds.left, bounds.top),
                       Vec2(bounds.size.x, BorderWidth))

  def draw() {
    artist.startDrawing()

    val dimensions = artist.dimensions
    val width = dimensions.x.asInstanceOf[Int]
    val height = dimensions.y.asInstanceOf[Int]

    val tileImage = images.get("BACKGROUND_TILE")
    val tileSize = Vec2(16f, 16f)

    for (x <- 0 until (width, 16)) {
      for (y <- 0 until (height, 16)) {
        artist.drawImage(Rect(Vec2(x, y), tileSize), tileImage)
      }
    }

    val offset = Vec2((dimensions.x - World.Width) / 2f,
                      (dimensions.y - World.Height) / 2f)

    val oldOffset = artist.setOffset(offset)

    artist.drawImage(Rect(Vec2.Zero, Vec2(World.Width, World.Height)), images.get("BLANK"), Color.BLACK)

    val paddle = world.paddle
    val ball = world.ball

    artist.drawImage(paddle.bounds, images.get("PADDLE"))
    artist.drawImage(ball.bounds, images.get("BALL"))

    world.bricks.foreach { brick => drawBrick(brick) }

    artist.drawImage(leftBorder, images.get("BORDER_SIDE"))
    artist.drawImage(rightBorder, images.get("BORDER_SIDE"))
    artist.drawImage(topBorder, images.get("BORDER_TOP"))
    artist.drawImage(leftCap, images.get("BORDER_CORNER"))
    artist.drawImage(rightCap, images.get("BORDER_CORNER"))

    if (world.isGameOver) {
      drawImageCentered(images.get("GAME_OVER"), world.bounds, 1.5f, 5.0f)
    } else if (world.isCleared) {
      drawImageCentered(images.get("CLEARED"), world.bounds, 1.5f, 5.0f)
    }

    artist.finishDrawing()
    artist.setOffset(oldOffset)
  }

  private def drawBrick(brick: Brick) {
    if (brick.isBreaking) {
      artist.drawImage(brick.bounds, images.frame("BRICK_BREAKING", brick.stateCounter), Color.RED)
    } else {
      artist.drawImage(brick.bounds, images.get("BRICK"), Color.RED)
    }
  }

  private def drawImageCentered(image: Image, bounds: Rect, widthRatio: Float, heightRatio: Float) {
    val size = Vec2(bounds.size.x / widthRatio, bounds.size.y / heightRatio)
    val origin = Vec2((bounds.size.x - size.x) / 2f, (bounds.size.y - size.y) / 2f)

    artist.drawImage(Rect(origin, size), image)
  }
}
