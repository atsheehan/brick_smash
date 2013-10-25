package com.bazbatlabs.bricksmash.controllers

import android.view.KeyEvent

import tv.ouya.console.api.OuyaController

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.views.WorldView

class WorldController(val artist: Artist, val images: ImageMap, val sounds: SoundMap) extends Controller {

  val events = new WorldEvents()
  val world = new World(events)
  val view = new WorldView(world, events, images, sounds, artist)

  override def draw(screenWidth: Int, screenHeight: Int) { view.draw() }

  override def update(): Controller = {
    world.update()
    this
  }

  override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = {
    var handled = true

    keyCode match {
      case OuyaController.BUTTON_DPAD_LEFT => world.startAcceleratingPaddle(Direction.Left)
      case OuyaController.BUTTON_DPAD_RIGHT => world.startAcceleratingPaddle(Direction.Right)
      case _ => handled = false
    }

    handled
  }

  override def onKeyUp(keyCode: Int, event: KeyEvent): Boolean = {
    var handled = true

    keyCode match {
      case OuyaController.BUTTON_DPAD_LEFT => world.stopAcceleratingPaddle(Direction.Left)
      case OuyaController.BUTTON_DPAD_RIGHT => world.stopAcceleratingPaddle(Direction.Right)
      case OuyaController.BUTTON_O => world.kickstartBall()
      case _ => handled = false
    }

    handled
  }

  override def changeSurface(width: Int, height: Int) {
    artist.changeSurface(width, height)
  }
}
