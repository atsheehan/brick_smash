package com.bazbatlabs.bricksmash.models

import com.bazbatlabs.bricksmash.lib._

import android.content.res.Resources
import com.bazbatlabs.bricksmash.R

class ImageMap(resources: Resources) {

  val texture = TextureLoader.load(R.raw.textures, resources)

  if (texture == null) {
    throw new RuntimeException("Failed to load texture. Check log for details.")
  }

  val images = Map(
    "BRICK" -> Image(0f, 0f, 40f, 12f, texture),
    "BRICK_1" -> Image(0f, 13f, 40f, 12f, texture),
    "BRICK_2" -> Image(0f, 26f, 40f, 12f, texture),
    "BRICK_3" -> Image(0f, 39f, 40f, 12f, texture),
    "BRICK_4" -> Image(0f, 52f, 40f, 12f, texture),
    "BALL" -> Image(0f, 156f, 8f, 8f, texture),
    "PADDLE" -> Image(0f, 188f, 48f, 12f, texture),
    "GAME_OVER" -> Image(74f, 65f, 128f, 16f, texture),
    "CLEARED" -> Image(74f, 82f, 104f, 16f, texture),
    "BORDER_CORNER" -> Image(41f, 0f, 6f, 6f, texture),
    "BORDER_TOP" -> Image(41f, 7f, 6f, 6f, texture),
    "BORDER_SIDE" -> Image(41f, 14f, 6f, 6f, texture),
    "TITLE" -> Image(2f, 120f, 187f, 16f, texture)
  )

  def get(name: String) = images(name)
}
