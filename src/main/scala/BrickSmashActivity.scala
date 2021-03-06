package com.bazbatlabs.bricksmash

import com.bazbatlabs.bricksmash.lib.{OuyaGame, Controller}
import com.bazbatlabs.bricksmash.controllers.InitController

class BrickSmashActivity extends OuyaGame {
  override def tag: String = "BrickSmash"

  override def initialController: Controller = new InitController(getResources)
}
