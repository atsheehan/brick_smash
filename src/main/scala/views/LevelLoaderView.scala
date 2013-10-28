package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.lib._

class LevelLoaderView(val level: Int, val artist: Artist, val images: ImageMap) {

  def draw() {
    artist.startDrawing()

    val dimensions = artist.dimensions

    val textImage = images.get("LEVEL")
    val numberImage = images.digits(level)

    val fontHeight = dimensions.y / 16f
    val textWidth = fontHeight * (textImage.w / textImage.h)
    val numberWidth = fontHeight * (numberImage.w / numberImage.h)

    val textSize = Vec2(textWidth, fontHeight)
    val numberSize = Vec2(numberWidth, fontHeight)

    val x = (dimensions.x - (textSize.x + (2 * numberSize.x))) / 2f
    val y = (dimensions.y - fontHeight) / 2f

    artist.drawImage(Rect(Vec2(x, y), textSize), textImage)
    artist.drawImage(Rect(Vec2(x + textSize.x + numberSize.x, y), numberSize), numberImage)

    artist.finishDrawing()
  }
}
