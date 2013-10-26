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

  val AColor = "a_Color"
  val APosition = "a_Position"
  val ATextureCoordinates = "a_TextureCoordinates"
  val UMatrix = "u_Matrix"
  val UTextureUnit = "u_TextureUnit"

  val Tag = "Artist"

  var texture = 0
  val program = ShaderCompiler.buildProgram(R.raw.vertex_shader, R.raw.fragment_shader, resources)

  if (program == 0) {
    throw new RuntimeException("Failed to build OpenGL program. Check log for details.")
  }

  glUseProgram(program)

  var aColorLoc = glGetAttribLocation(program, AColor)
  var aPositionLoc = glGetAttribLocation(program, APosition)
  var aTextureCoordinatesLoc = glGetAttribLocation(program, ATextureCoordinates)

  var uMatrixLoc = glGetUniformLocation(program, UMatrix)
  var uTextureUnitLoc = glGetUniformLocation(program, UTextureUnit)

  val projectionMatrix = new Array[Float](16)

  changeSurface(screenWidth, screenHeight)

  val vertexBuffer = ByteBuffer.allocateDirect(VertexBufferSize)
    .order(ByteOrder.nativeOrder()).asFloatBuffer()
  val vertices = new Array[Float](VertexArraySize)

  val indexBuffer = ByteBuffer.allocateDirect(IndexBufferSize)
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

  indexBuffer.put(indices, 0, indices.length)
  indexBuffer.flip()

  private var vertexIndex = 0
  private var spriteCount = 0

  vertexBuffer.position(PositionStart)
  glVertexAttribPointer(aPositionLoc, FloatsPerPosition, GL_FLOAT,
                        false, BytesPerVertex, vertexBuffer)
  glEnableVertexAttribArray(aPositionLoc)

  vertexBuffer.position(TextureStart)
  glVertexAttribPointer(aTextureCoordinatesLoc, FloatsPerTexture, GL_FLOAT,
                        false, BytesPerVertex, vertexBuffer)
  glEnableVertexAttribArray(aTextureCoordinatesLoc)

  vertexBuffer.position(ColorStart)
  glVertexAttribPointer(aColorLoc, FloatsPerColor, GL_FLOAT,
                        false, BytesPerVertex, vertexBuffer)

  glEnableVertexAttribArray(aColorLoc)
  glViewport(0, 0, screenWidth, screenHeight)

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
    vertexIndex = 0
    spriteCount = 0
  }

  def finishDrawing() {
    vertexBuffer.clear()
    vertexBuffer.put(vertices)

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

    glClear(GL_COLOR_BUFFER_BIT)

    glUniformMatrix4fv(uMatrixLoc, 1, false, projectionMatrix, 0)

    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, texture)
    glUniform1i(uTextureUnitLoc, 0)

    glDrawElements(GL_TRIANGLES, IndicesPerSprite * spriteCount, GL_UNSIGNED_SHORT, indexBuffer)
  }

  def drawRect(bounds: Rect, color: Color) {
    drawRect(bounds.origin, bounds.size, color)
  }

  def drawRect(origin: Vec2, size: Vec2, color: Color) {
    addVertex(origin.x, origin.y, color)
    addVertex(origin.x + size.x, origin.y, color)
    addVertex(origin.x + size.x, origin.y + size.y, color)
    addVertex(origin.x, origin.y + size.y, color)

    spriteCount += 1
  }

  def drawImage(bounds: Rect, image: Image) {
    drawImage(bounds.origin, bounds.size, image, Color.WHITE)
  }

  def drawImage(bounds: Rect, image: Image, color: Color) {
    drawImage(bounds.origin, bounds.size, image, color)
  }

  def drawImage(origin: Vec2, size: Vec2, image: Image) {
    drawImage(origin, size, image, Color.WHITE)
  }

  def drawImage(origin: Vec2, size: Vec2, image: Image, color: Color) {
    addVertex(origin.x, origin.y, image.x, image.y + image.h, color)
    addVertex(origin.x + size.x, origin.y, image.x + image.w, image.y + image.h, color)
    addVertex(origin.x + size.x, origin.y + size.y, image.x + image.w, image.y, color)
    addVertex(origin.x, origin.y + size.y, image.x, image.y, color)

    texture = image.textureId

    spriteCount += 1
  }

  def addVertex(x: Float, y: Float) {
    addVertex(x, y, Color.WHITE)
  }

  def addVertex(x: Float, y: Float, color: Color) {
    addVertex(x, y, 0f, 0f, color)
  }

  def addVertex(x: Float, y: Float, texX: Float, texY: Float) {
    addVertex(x, y, texX, texY, Color.WHITE)
  }

  def addVertex(x: Float, y: Float, texX: Float, texY: Float, color: Color) {
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
  def gameDimensions(screenWidth: Float, screenHeight: Float) =
    if (screenHeight == 0.0f) {
      Vec2.Zero
    } else {
      val ratio = screenWidth / screenHeight
      Vec2(GameHeight * ratio, GameHeight)
    }


}
