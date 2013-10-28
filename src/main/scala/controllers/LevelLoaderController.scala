package com.bazbatlabs.bricksmash.controllers

import java.io._

import android.view.KeyEvent
import android.content.res.{AssetManager, Resources}
import android.media.{AudioManager, SoundPool}

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.views._

class LevelLoaderController(val level: Int, val artist: Artist, val images: ImageMap,
                            val sounds: SoundMap, resources: Resources) extends Controller {

  val Duration = 100

  private var counter = 0
  private var isLoaded = false

  val view = new LevelLoaderView(level, artist, images)

  override def draw(screenWidth: Int, screenHeight: Int) { view.draw() }

  override def update(): Controller = {
    counter += 1

    if (!isLoaded) {
      isLoaded = true
    }

    if (isLoaded && counter > Duration) {
      new WorldController(artist, images, sounds, resources)
    } else {
      this
    }
  }

  override def changeSurface(width: Int, height: Int) {
    artist.changeSurface(width, height)
  }

  override def onKeyDown(keyCode: Int, event: KeyEvent) = false
  override def onKeyUp(keyCode: Int, event: KeyEvent) = false
}
