package com.bazbatlabs.bricksmash.controllers

import scala.io.Source

import android.view.KeyEvent

import tv.ouya.console.api.OuyaController
import android.content.res.Resources

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.views._
import com.bazbatlabs.bricksmash.views.WorldView
import com.bazbatlabs.bricksmash.R

class WorldController(val artist: Artist, val images: ImageMap,
                      val sounds: SoundMap, resources: Resources) extends Controller {

  val events = new WorldEvents()
  val world = loadWorldFromResource(R.raw.levels)
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

  private def loadWorldFromResource(resourceId: Int): World = {
    val brickLayoutString = try {
      val inputStream = resources.openRawResource(resourceId)
      Source.fromInputStream(inputStream).mkString("")
    } catch {
      case ex: Resources.NotFoundException =>
        throw new RuntimeException("Resource not found: " + R.raw.levels, ex)
    }

    val brickLayout = brickLayoutString.split("\\s+").map(
      x => x match {
        case "0" => None
        case "1" => Some(Brick.Type.Normal)
        case "2" => Some(Brick.Type.Tough)
      }
    )

    new World(brickLayout, events)
  }
}
