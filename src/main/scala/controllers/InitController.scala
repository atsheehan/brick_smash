package com.bazbatlabs.bricksmash.controllers

import java.io._

import javax.microedition.khronos.opengles.GL10
import android.view.KeyEvent
import android.content.res.{AssetManager, Resources}
import android.media.{AudioManager, SoundPool}

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._

class InitController(val resources: Resources) extends Controller {

  var finished = false

  var artist: Artist = null
  var images: ImageMap = null
  var sounds: SoundMap = null

  override def draw(screenWidth: Int, screenHeight: Int) {
    if (!finished) {
      if (artist == null) {
        artist = new Artist(resources, screenWidth, screenHeight)
      }

      if (images == null) {
        images = new ImageMap(resources)
      }

      if (sounds == null) {
        sounds = new SoundMap(resources)
      }

      finished = true
    }
  }

  override def update(): Controller = {
    if (finished) {
      val worldController = new WorldController(artist, images, sounds)
      new MenuController(artist, images, sounds, worldController)
    } else {
      this
    }
  }

  override def changeSurface(width: Int, height: Int) {
    if (artist != null) {
      artist.changeSurface(width, height)
    }
  }

  override def onKeyDown(keyCode: Int, event: KeyEvent) = false
  override def onKeyUp(keyCode: Int, event: KeyEvent) = false
}
