package com.bazbatlabs.bricksmash.controllers

import android.view.KeyEvent
import android.content.res.Resources

import tv.ouya.console.api.OuyaController

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.views._
import com.bazbatlabs.bricksmash.views.MenuView

class MenuController(val artist: Artist, val images: ImageMap,
                     val sounds: SoundMap, val resources: Resources) extends Controller {

  val menu = new Menu()
  val view = new MenuView(menu, images, sounds, artist)

  override def draw(screenWidth: Int, screenHeight: Int) { view.draw() }

  override def update(): Controller = {
    menu.update()

    if (menu.isReadyToStartGame) {
      new WorldController(artist, images, sounds, resources)
    } else {
      this
    }
  }

  override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = {
    var handled = true

    keyCode match {
      case OuyaController.BUTTON_DPAD_UP => menu.previousEntry()
      case OuyaController.BUTTON_DPAD_DOWN => menu.nextEntry()
      case OuyaController.BUTTON_O => menu.select()
      case _ => handled = false
    }

    handled
  }

  override def onKeyUp(keyCode: Int, event: KeyEvent) = false

  override def changeSurface(width: Int, height: Int) {
    artist.changeSurface(width, height)
  }
}
