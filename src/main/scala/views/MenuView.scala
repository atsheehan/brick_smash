package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.models._

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

    artist.drawImage(Vec2(100f, 200f), Vec2(200f, 20f), images.get("TITLE"), color)
    artist.finishDrawing()

    counter += 1
  }
}
