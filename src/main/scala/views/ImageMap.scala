package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.lib._

import android.content.res.Resources
import com.bazbatlabs.bricksmash.R

class ImageMap(resources: Resources) {

  val texture = TextureLoader.load(R.raw.textures, resources)

  if (texture == null) {
    throw new RuntimeException("Failed to load texture. Check log for details.")
  }

  val images = Map(
    "BRICK" -> Image(48f, 0f, 16f, 8f, texture),
    "BALL" -> Image(0f, 156f, 8f, 8f, texture),
    "PADDLE" -> Image(48f, 9f, 26f, 8f, texture),
    "GAME_OVER" -> Image(75f, 66f, 122f, 14f, texture),
    "CLEARED" -> Image(75f, 83f, 102f, 14f, texture),
    "BORDER_CORNER" -> Image(41f, 0f, 6f, 6f, texture),
    "BORDER_TOP" -> Image(41f, 7f, 6f, 6f, texture),
    "BORDER_SIDE" -> Image(41f, 14f, 6f, 6f, texture),
    "TITLE" -> Image(2f, 120f, 187f, 16f, texture),
    "BACKGROUND_TILE" -> Image(48f, 18f, 16f, 16f, texture),
    "BLANK" -> Image(48f, 35f, 2f, 2f, texture),
    "LEVEL" -> Image(1f, 102f, 70f, 14f, texture)
  )

  val DigitWidth = 14f
  val DigitHeight = 14f
  val DigitPadding = 2f

  val digits = (0 to 9).map {
    num =>
      Image(1f + (num * (DigitWidth + DigitPadding)), 140f,
            DigitWidth, DigitHeight, texture)
  }

  val animations = Map(
    "BRICK_BREAKING" -> (4, Array(
      Image(0f, 0f, 40f, 12f, texture),
      Image(0f, 13f, 40f, 12f, texture),
      Image(0f, 26f, 40f, 12f, texture),
      Image(0f, 39f, 40f, 12f, texture),
      Image(0f, 52f, 40f, 12f, texture)
    ))
  )

  def get(name: String): Image = images(name)

  def frame(name: String, counter: Int): Image = {
    val (frameDuration, frames) = animations(name)
    val totalDuration = frameDuration * frames.length
    val index = (counter % totalDuration) / frameDuration
    frames(index)
  }
}
