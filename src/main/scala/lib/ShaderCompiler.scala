package com.bazbatlabs.bricksmash.lib

import scala.io.Source

import android.util.Log
import android.content.res.Resources

import android.opengl.GLES20._

object ShaderCompiler {

  def buildProgram(vertexResourceId: Int, fragmentResourceId: Int, resources: Resources): Int = {
    val vertexSource = readResource(resources, vertexResourceId)
    val fragmentSource = readResource(resources, fragmentResourceId)

    // TODO: check for error return values
    val vertexShader = compileShader(GL_VERTEX_SHADER, vertexSource)
    if (vertexShader == 0) {
      Log.w(Tag, "Compilation failure for vertex shader.")
      return 0
    }

    val fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentSource)
    if (fragmentShader == 0) {
      Log.w(Tag, "Compilation failure for fragment shader.")
      glDeleteShader(vertexShader)
      return 0
    }

    val program = linkProgram(vertexShader, fragmentShader)

    glDeleteShader(vertexShader)
    glDeleteShader(fragmentShader)

    if (program == 0) {
      Log.w(Tag, "Linking failure.")
      return 0
    }

    if (!validateProgram(program)) {
      Log.w(Tag, "Program failed validation.")
      glDeleteProgram(program)
      return 0
    }

    program
  }

  def readResource(resources: Resources, id: Int): String = {
    try {
      val inputStream = resources.openRawResource(id)
      Source.fromInputStream(inputStream).mkString("")

    } catch {
      case ex: Resources.NotFoundException =>
        throw new RuntimeException("Resource not found: " + id, ex)
    }
  }

  def compileShader(shaderType: Int, code: String): Int = {

    val shaderId = glCreateShader(shaderType)

    if (shaderId == 0) {
      Log.w(Tag, "Could not create new shader.")
      return 0
    }

    glShaderSource(shaderId, code)
    glCompileShader(shaderId)

    val status = new Array[Int](1)
    glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0)

    Log.v(Tag, "Shader compilation status: " + glGetShaderInfoLog(shaderId))

    if (status(0) == 0) {
      glDeleteShader(shaderId)
      Log.w(Tag, "Shader compilation failed.")

      return 0
    }

    shaderId
  }

  def linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int = {

    val programId = glCreateProgram()

    if (programId == 0) {
      Log.w(Tag, "Could not create new program.")
      return 0
    }

    glAttachShader(programId, vertexShaderId)
    glAttachShader(programId, fragmentShaderId)

    glLinkProgram(programId)

    val status = new Array[Int](1)
    glGetProgramiv(programId, GL_LINK_STATUS, status, 0)

    Log.v(Tag, "Program link status: " + glGetProgramInfoLog(programId))

    if (status(0) == 0) {
      glDeleteProgram(programId)
      Log.w(Tag, "Program linking failed.")

      return 0
    }

    programId
  }

  def validateProgram(programId: Int): Boolean = {
    glValidateProgram(programId)

    val status = new Array[Int](1)
    glGetProgramiv(programId, GL_VALIDATE_STATUS, status, 0)

    Log.v(Tag, "Validation status: " + status(0) +
          "\nLog: " + glGetProgramInfoLog(programId))

    return status(0) != 0
  }

  val Tag = "ShaderCompiler"
}
