package com.bazbatlabs.bricksmash

import android.util.Log
import android.os.{Bundle, SystemClock}
import android.app.Activity
import android.opengl.GLSurfaceView
import android.view.{Window, WindowManager, KeyEvent}
import android.opengl.{GLSurfaceView, GLES20}
import android.media.AudioManager

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGLConfig

import tv.ouya.console.api.OuyaController
import tv.ouya.console.api.OuyaController._

import com.bazbatlabs.bricksmash.controllers._

class BrickSmashActivity extends Activity with GLSurfaceView.Renderer {

  val TAG = "BrickSmashActivity"
  val LOG_FPS_INTERVAL = 100
  val NANOSECONDS_PER_SECOND = 1000000000
  val UPDATES_PER_SECOND = 60

  val UPDATE_DELTA = NANOSECONDS_PER_SECOND / UPDATES_PER_SECOND

  var fpsStart = 0L
  var fpsFrames = 0L
  var ticksLastFrame = 0L
  var tickCounter = 0L

  var controller: Controller = null
  var view: GLSurfaceView = null

  var rendererSet = false

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

    controller = new InitController(getResources)
    OuyaController.init(this)

    ticksLastFrame = System.nanoTime
    fpsStart = SystemClock.elapsedRealtime
  }

  override def onDrawFrame(gl: GL10) {
    val currentTime = System.nanoTime
    tickCounter += currentTime - ticksLastFrame
    ticksLastFrame = currentTime

    controller.draw(view.getWidth, view.getHeight)

    while (tickCounter > UPDATE_DELTA) {
      controller = controller.update()
      tickCounter -= UPDATE_DELTA
    }

    logFramerate()
  }

  override def onSurfaceChanged(unused: GL10, width: Int, height: Int) {
    GLES20.glViewport(0, 0, width, height)
  }

  override def onSurfaceCreated(gl: GL10, config: EGLConfig) {}

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
    if (isOuyaKey(keyCode)) controller.onKeyDown(keyCode, event)
    else super.onKeyDown(keyCode, event)

  override def onKeyUp(keyCode: Int, event: KeyEvent): Boolean =
    if (isOuyaKey(keyCode)) controller.onKeyUp(keyCode, event)
    else super.onKeyUp(keyCode, event)

  private def isOuyaKey(keyCode: Int): Boolean =
    keyCode match {
      case BUTTON_O | BUTTON_U | BUTTON_Y | BUTTON_A | BUTTON_R1 |
         BUTTON_R2 | BUTTON_R3 | BUTTON_L1 | BUTTON_L2 | BUTTON_L3 |
         BUTTON_DPAD_DOWN | BUTTON_DPAD_LEFT | BUTTON_DPAD_RIGHT |
         BUTTON_DPAD_UP | BUTTON_MENU => true
      case _ => false
    }

  private def logFramerate() {
    fpsFrames += 1

    if (fpsFrames % LOG_FPS_INTERVAL == 0) {
      val fpsEnd = SystemClock.elapsedRealtime
      val fps = (fpsEnd - fpsStart).asInstanceOf[Double] / fpsFrames.asInstanceOf[Double]

      Log.d(TAG, f"FPS: $fps%.2f")

      fpsStart = fpsEnd
      fpsFrames = 0
    }
  }
}
