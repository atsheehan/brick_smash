package com.bazbatlabs.bricksmash.controllers

import java.io._
import scala.io.Source

import android.view.KeyEvent
import android.content.res.{AssetManager, Resources}
import android.media.{AudioManager, SoundPool}

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.views._
import com.bazbatlabs.bricksmash.R

class LevelLoaderController(val level: Int, val artist: Artist, val images: ImageMap,
                            val sounds: SoundMap, resources: Resources) extends Controller {

  val Duration = 100

  private var counter = 0
  private var isLoaded = false
  private var brickLayout: Array[Array[Option[Brick.Type.Value]]] = null

  val view = new LevelLoaderView(level, artist, images)

  override def draw(screenWidth: Int, screenHeight: Int) { view.draw() }

  override def update(): Controller = {
    counter += 1

    if (!isLoaded) {
      brickLayout = loadBrickLayout(level - 1)
      isLoaded = true
    }

    if (isLoaded && counter > Duration) {
      new WorldController(brickLayout, level, artist, images, sounds, resources)
    } else {
      this
    }
  }

  override def changeSurface(width: Int, height: Int) {
    artist.changeSurface(width, height)
  }

  override def onKeyDown(keyCode: Int, event: KeyEvent) = false
  override def onKeyUp(keyCode: Int, event: KeyEvent) = false

  private def loadBrickLayout(level: Int): Array[Array[Option[Brick.Type.Value]]] = {

    val layoutContents = try {
      val inputStream = resources.openRawResource(R.raw.levels)
      Source.fromInputStream(inputStream).mkString("")
    } catch {
      case ex: Resources.NotFoundException =>
        throw new RuntimeException("Resource not found: " + R.raw.levels, ex)
    }

    val layoutsPerLevel = layoutContents.split("#\n")

    val layoutLines = layoutsPerLevel(level).split("\n")

    layoutLines.map {
      line =>
        line.trim.split("\\s+").map(
          x => x match {
            case "0" => None
            case "1" => Some(Brick.Type.Normal)
            case "2" => Some(Brick.Type.Tough)
          }
        )
    }
  }
}
