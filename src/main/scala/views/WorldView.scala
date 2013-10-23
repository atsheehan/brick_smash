package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.models.WorldEvents.Event

class WorldView(val world: World, val events: WorldEvents, val images: ImageMap, val sounds: SoundMap, val artist: Artist) {

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

  def draw {

    while (events.hasNext) {
      val event = events.dequeue()

      event match {
        case Event.WALL_HIT | Event.BRICK_HIT => sounds.play(Blip)
        case Event.PADDLE_HIT => sounds.play(BlipLow)
      }
    }

    val paddle = world.paddle
    val ball = world.ball

    artist.startDrawing()

    artist.drawImage(paddle.pos, paddle.size, images.get("PADDLE"))
    artist.drawImage(ball.pos, ball.size, images.get("BALL"))

    val brickImages = Array[Image](
      images.get("BRICK"),
      images.get("BRICK_1"),
      images.get("BRICK_2"),
      images.get("BRICK_3"),
      images.get("BRICK_4")
    )

    for (brick <- world.bricks) {
      if (brick.isBreaking) {
        val index = brick.stateCounter / 4
        artist.drawImage(brick.bounds, brickImages(index), Color.RED)

      } else {
        artist.drawImage(brick.bounds, brickImages(0), Color.RED)
      }
    }

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
  }

  private def drawImageCentered(image: Image, bounds: Rect, widthRatio: Float, heightRatio: Float) {
    val size = Vec2(bounds.size.x / widthRatio, bounds.size.y / heightRatio)
    val origin = Vec2((bounds.size.x - size.x) / 2f, (bounds.size.y - size.y) / 2f)

    artist.drawImage(Rect(origin, size), image)
  }
}
