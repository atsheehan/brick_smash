package com.bazbatlabs.bricksmash.views

import com.bazbatlabs.bricksmash.lib._

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLUtils
import android.util.Log

import android.opengl.GLES20._

object TextureLoader {

  def load(resourceId: Int, resources: Resources): Texture = {

    val textureIds = new Array[Int](1)
    glGenTextures(1, textureIds, 0)

    if (textureIds(0) == 0) {
      Log.w(Tag, "Could not create new OpenGL texture object.")
      return null
    }

    val options = new BitmapFactory.Options()
    options.inScaled = false

    val bitmap = BitmapFactory.decodeResource(resources, resourceId, options)

    if (bitmap == null) {
      Log.w(Tag, "Resource ID " + resourceId + " could not be decoded.")

      glDeleteTextures(1, textureIds, 0)
      return null
    }

    val width = bitmap.getWidth()
    val height = bitmap.getHeight()

    glBindTexture(GL_TEXTURE_2D, textureIds(0))

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

    GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

    bitmap.recycle()

    glBindTexture(GL_TEXTURE_2D, 0)

    new Texture(textureIds(0), width, height)
  }

  val Tag = "TextureLoader"
}
