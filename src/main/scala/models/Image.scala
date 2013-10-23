package com.bazbatlabs.bricksmash.models

class Image(tx: Float, ty: Float, tw: Float, th: Float, texture: Texture) {
  val textureId: Int = texture.id

  val x: Float = tx / texture.width
  val y: Float = ty / texture.height
  val w: Float = tw / texture.width
  val h: Float = th / texture.height
}

object Image {
  def apply(tx: Float, ty: Float, tw: Float, th: Float, texture: Texture) =
    new Image(tx, ty, tw, th, texture)
}
