package com.bazbatlabs.bricksmash

import com.bazbatlabs.bricksmash.lib.OuyaGame
import com.bazbatlabs.bricksmash.controllers._

class BrickSmashActivity extends OuyaGame {
  override def tag: String = "BrickSmash"

  override def initialController: Controller = new InitController(getResources)
}
