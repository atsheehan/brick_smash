package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.lib._

class MenuView(val menu: Menu, val images: ImageMap, val sounds: SoundMap, val artist: Artist) {

  val CycleLength = 200
  val CycleCoefficient =
    (2 * Math.PI).asInstanceOf[Float] / CycleLength.asInstanceOf[Float]

  var r = 1f
  var g = 1f
  var b = 1f
  var counter = 0

  def draw() {
    artist.startDrawing()

    val radians = CycleCoefficient * (counter % CycleLength)
    r = Math.abs(Math.sin(radians)).asInstanceOf[Float]
    b = r
    g = Math.abs(Math.cos(radians)).asInstanceOf[Float]
    val color = Color(r, g, b, 1f)

    val titleImage = images.get("TITLE")
    val dimensions = artist.dimensions

    val titleHeight = dimensions.y / 12f
    val titleWidth = titleHeight * (titleImage.w / titleImage.h)

    val titleSize = Vec2(titleWidth, titleHeight)

    val x = (dimensions.x - titleSize.x) / 2f
    val y = (dimensions.y - titleSize.y) / 2f

    artist.drawImage(Rect(Vec2(x, y), titleSize), titleImage, color)
    artist.finishDrawing()

    counter += 1
  }
}
