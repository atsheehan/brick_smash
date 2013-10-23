package com.bazbatlabs.bricksmash.models

class Menu {

  var startGame = false

  def update() {}
  def nextEntry() {}
  def previousEntry() {}
  def select() { startGame = true }

  def isReadyToStartGame = startGame
}
