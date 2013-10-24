package com.bazbatlabs.bricksmash.models

trait Collidable {
  def bounds: Rect
  def hit()
  def deflect(vel: Vec2, collision: Vec2, axis: Axis.Value): Vec2
  def isActive: Boolean
}
