package com.bazbatlabs.bricksmash.models

import java.io.IOException

import android.content.res.Resources
import android.media.AudioManager
import android.media.SoundPool

class SoundMap(resources: Resources) {
  val MaxStreams = 20

  val soundPool = new SoundPool(MaxStreams, AudioManager.STREAM_MUSIC, 0)

  val map = Map[Sound, Int](
    Blip -> loadSound(Blip.filename),
    BlipLow -> loadSound(BlipLow.filename)
  )

  def loadSound(filename: String): Int =
    try {
      soundPool.load(resources.getAssets.openFd("sounds/" + filename), 1)
    } catch {
      case ex: IOException => throw new RuntimeException("Failed to load asset: " + filename, ex)
    }

  def play(sound: Sound) {
    soundPool.play(map(sound), 1, 1, 0, 0, 1)
  }
}

sealed abstract class Sound(val filename: String)
case object Blip extends Sound("blip.ogg")
case object BlipLow extends Sound("blip_low.ogg")
