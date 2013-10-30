package com.bazbatlabs.bricksmash.lib

import android.util.Log
import android.os.{Bundle, SystemClock}
import android.app.Activity
import android.view.{Window, WindowManager, KeyEvent}
import android.opengl.{GLSurfaceView, GLES20}
import android.media.AudioManager

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGLConfig

import tv.ouya.console.api.OuyaController
import tv.ouya.console.api.OuyaController._

abstract class OuyaGame extends Activity with GLSurfaceView.Renderer {

  protected def tag: String = "OuyaGame"

  private val LogFpsInterval = 100
  private val NanosecondsPerSecond = 1000000000
  private val UpdatesPerSecond = 60

  private val UpdateDelta = NanosecondsPerSecond / UpdatesPerSecond

  private var fpsStart = 0L
  private var fpsFrames = 0L
  private var ticksLastFrame = 0L
  private var tickCounter = 0L

  private var view: GLSurfaceView = null
  private var rendererSet = false

  private var controller: Controller = null

  def initialController: Controller

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    requestWindowFeature(Window.FEATURE_NO_TITLE)
    getWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                       WindowManager.LayoutParams.FLAG_FULLSCREEN)

    setVolumeControlStream(AudioManager.STREAM_MUSIC)

    view = new GLSurfaceView(this)
    view.setEGLContextClientVersion(2)
    view.setRenderer(this)
    rendererSet = true

    setContentView(view)
    controller = initialController

    OuyaController.init(this)

    ticksLastFrame = System.nanoTime
    fpsStart = SystemClock.elapsedRealtime
  }

  override def onDrawFrame(unused: GL10) {
    val currentTime = System.nanoTime

    tickCounter += currentTime - ticksLastFrame
    ticksLastFrame = currentTime

    while (tickCounter > UpdateDelta) {
      controller = controller.update()
      tickCounter -= UpdateDelta
    }

    controller.draw(view.getWidth, view.getHeight)

    logFramerate()
  }

  override def onSurfaceChanged(unused: GL10, width: Int, height: Int) {
    GLES20.glViewport(0, 0, width, height)
  }

  override def onSurfaceCreated(unused: GL10, config: EGLConfig) {}

  override def onPause() {
    super.onPause()

    if (rendererSet) {
      view.onPause()
    }
  }

  override def onResume() {
    super.onResume()

    if (rendererSet) {
      view.onResume()
    }
  }

  override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean =
    if (isOuyaKey(keyCode)) { controller.onKeyDown(keyCode, event) }
    else { super.onKeyDown(keyCode, event) }

  override def onKeyUp(keyCode: Int, event: KeyEvent): Boolean =
    if (isOuyaKey(keyCode)) { controller.onKeyUp(keyCode, event) }
    else { super.onKeyUp(keyCode, event) }

  private def isOuyaKey(keyCode: Int): Boolean =
    keyCode match {
      case
        BUTTON_O | BUTTON_U | BUTTON_Y | BUTTON_A |
        BUTTON_R1 | BUTTON_R2 | BUTTON_R3 |
        BUTTON_L1 | BUTTON_L2 | BUTTON_L3 |
        BUTTON_DPAD_DOWN | BUTTON_DPAD_LEFT |
        BUTTON_DPAD_RIGHT | BUTTON_DPAD_UP | BUTTON_MENU => true
      case _ => false
    }

  private def logFramerate() {
    fpsFrames += 1

    if (fpsFrames % LogFpsInterval == 0) {
      val fpsEnd = SystemClock.elapsedRealtime
      val fps = (fpsEnd - fpsStart).asInstanceOf[Double] / fpsFrames.asInstanceOf[Double]

      Log.d(tag, f"FPS: $fps%.2f")

      fpsStart = fpsEnd
      fpsFrames = 0
    }
  }
}
