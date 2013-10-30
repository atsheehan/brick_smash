package com.bazbatlabs.bricksmash.lib

import javax.microedition.khronos.opengles.GL10
import android.view.KeyEvent

trait Controller {
  def draw(screenWidth: Int, screenHeight: Int)
  def update(): Controller
  def onKeyDown(keyCode: Int, event: KeyEvent): Boolean
  def onKeyUp(keyCode: Int, event: KeyEvent): Boolean
  def changeSurface(width: Int, height: Int)
}
