package com.bazbatlabs.bricksmash.views

import java.nio.{ByteBuffer, ByteOrder, ShortBuffer, FloatBuffer}

import android.util.Log
import android.content.res.Resources
import android.opengl.Matrix
import android.opengl.GLES20._

import com.bazbatlabs.bricksmash.lib._
import com.bazbatlabs.bricksmash.models._
import com.bazbatlabs.bricksmash.R

class Artist(resources: Resources, screenWidth: Int, screenHeight: Int) {

  val GameHeight = World.Height * 1.2f
  val GameWidth = World.Width * 1.2f
  val FloatsPerPosition = 2
  val FloatsPerTexture = 2
  val FloatsPerColor = 4
  val PositionStart = 0
  val TextureStart = PositionStart + FloatsPerPosition
  val ColorStart = TextureStart + FloatsPerTexture
  val FloatsPerVertex = FloatsPerPosition + FloatsPerTexture + FloatsPerColor
  val BytesPerFloat = 4
  val BytesPerShort = 2
  val BytesPerVertex = FloatsPerVertex * BytesPerFloat
  val VerticesPerSprite = 4
  val IndicesPerSprite = 6
  val MaxSprites = 1000
  val MaxVertices = MaxSprites * VerticesPerSprite
  val MaxIndices = MaxSprites * IndicesPerSprite
  val VertexBufferSize = MaxVertices * BytesPerVertex
  val VertexArraySize = MaxVertices * FloatsPerVertex
  val IndexBufferSize = MaxIndices * BytesPerShort
  val Tag = "Artist"

  private var texture = 0
  private val program = ShaderCompiler.buildProgram(R.raw.vertex_shader, R.raw.fragment_shader, resources)

  if (program == 0) {
    throw new RuntimeException("Failed to build OpenGL program. Check log for details.")
  }

  private val projectionMatrix = new Array[Float](16)

  private val vertexBuffer = ByteBuffer.allocateDirect(VertexBufferSize)
    .order(ByteOrder.nativeOrder()).asFloatBuffer()
  private val vertices = new Array[Float](VertexArraySize)

  private val indexBuffer = buildIndexBuffer

  private var vertexIndex = 0
  private var spriteCount = 0

  changeSurface(screenWidth, screenHeight)

  def changeSurface(width: Int, height: Int) {
    val dimensions = gameDimensions(width, height)

    val xOffset = (dimensions.x - World.Width) / 2f
    val yOffset = (dimensions.y - World.Height) / 2f

    val left = -xOffset
    val right = World.Width + xOffset
    val bottom = -yOffset
    val top = World.Height + yOffset

    Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, -1f, 1f)
    glViewport(0, 0, width, height)
  }

  def startDrawing() {
    resetCounters()
  }

  def finishDrawing() {
    clearScreen()

    enableProgram()
    setVertices()
    setAttributes()
    setUniforms()

    draw()
  }

  def drawImage(bounds: Rect, image: Image, color: Color = Color.WHITE) {
    val origin = bounds.origin
    val size = bounds.size

    addVertex(origin.x, origin.y, image.x, image.y + image.h, color)
    addVertex(origin.x + size.x, origin.y, image.x + image.w, image.y + image.h, color)
    addVertex(origin.x + size.x, origin.y + size.y, image.x + image.w, image.y, color)
    addVertex(origin.x, origin.y + size.y, image.x, image.y, color)

    texture = image.textureId

    spriteCount += 1
  }

  private def addVertex(x: Float, y: Float, texX: Float, texY: Float, color: Color) {
    vertices(vertexIndex) = x; vertexIndex += 1
    vertices(vertexIndex) = y; vertexIndex += 1
    vertices(vertexIndex) = texX; vertexIndex += 1
    vertices(vertexIndex) = texY; vertexIndex += 1
    vertices(vertexIndex) = color.r; vertexIndex += 1
    vertices(vertexIndex) = color.g; vertexIndex += 1
    vertices(vertexIndex) = color.b; vertexIndex += 1
    vertices(vertexIndex) = color.a; vertexIndex += 1
  }

  // Determine how much of the game world should be shown based on the screen
  // dimensions. Used to prevent the game objects from being stretched on
  // different screen width/height ratios. In this case, the height is constant
  // but the width will vary.
  private def gameDimensions(screenWidth: Float, screenHeight: Float) =
    if (screenHeight == 0.0f) {
      Vec2.Zero
    } else {
      val ratio = screenWidth / screenHeight
      Vec2(GameHeight * ratio, GameHeight)
    }

  private def resetCounters() {
    vertexIndex = 0
    spriteCount = 0
  }

  private def clearScreen() {
    glClearColor(0f, 0f, 0f, 0f)
    glClear(GL_COLOR_BUFFER_BIT)
  }

  private def enableProgram() {
    glUseProgram(program)
  }

  private def setVertices() {
    vertexBuffer.clear()
    vertexBuffer.put(vertices)
  }

  private def buildIndexBuffer: ShortBuffer = {
    val buffer = ByteBuffer.allocateDirect(IndexBufferSize)
      .order(ByteOrder.nativeOrder()).asShortBuffer()
    val indices = new Array[Short](MaxIndices)

    var j = 0
    for (i <- 0 until (indices.length, 6)) {
      indices(i + 0) = (j + 0).asInstanceOf[Short]
      indices(i + 1) = (j + 1).asInstanceOf[Short]
      indices(i + 2) = (j + 2).asInstanceOf[Short]
      indices(i + 3) = (j + 2).asInstanceOf[Short]
      indices(i + 4) = (j + 3).asInstanceOf[Short]
      indices(i + 5) = (j + 0).asInstanceOf[Short]

      j += 4
    }

    buffer.put(indices, 0, indices.length)
    buffer.flip()
    buffer
  }

  private def setAttributes() {
    val (colorAttr, posAttr, textureAttr) = attributeLocations

    vertexBuffer.position(PositionStart)
    glVertexAttribPointer(posAttr, FloatsPerPosition, GL_FLOAT,
                          false, BytesPerVertex, vertexBuffer)
    glEnableVertexAttribArray(posAttr)

    vertexBuffer.position(TextureStart)
    glVertexAttribPointer(textureAttr, FloatsPerTexture, GL_FLOAT,
                          false, BytesPerVertex, vertexBuffer)
    glEnableVertexAttribArray(textureAttr)

    vertexBuffer.position(ColorStart)
    glVertexAttribPointer(colorAttr, FloatsPerColor, GL_FLOAT,
                          false, BytesPerVertex, vertexBuffer)
    glEnableVertexAttribArray(colorAttr)
  }

  private def setUniforms() {
    val (matrixUniform, textureUniform) = uniformLocations

    glUniformMatrix4fv(matrixUniform, 1, false, projectionMatrix, 0)

    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, texture)
    glUniform1i(textureUniform, 0)
  }

  private def attributeLocations: (Int, Int, Int) = {
    (glGetAttribLocation(program, "a_Color"),
     glGetAttribLocation(program, "a_Position"),
     glGetAttribLocation(program, "a_TextureCoordinates"))
  }

  private def uniformLocations: (Int, Int) = {
    (glGetUniformLocation(program, "u_Matrix"),
     glGetUniformLocation(program, "u_TextureUnit"))
  }

  private def draw() {
    glDrawElements(GL_TRIANGLES, IndicesPerSprite * spriteCount,
                   GL_UNSIGNED_SHORT, indexBuffer)
  }
}
