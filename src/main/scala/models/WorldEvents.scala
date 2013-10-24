package com.bazbatlabs.bricksmash.models

import java.util.Queue
import java.util.LinkedList

class WorldEvents {

  val queue = new LinkedList[Event.Value]()

  def enqueue(event: Event.Value) {
    queue.add(event)
  }

  def dequeue(): Event.Value = queue.remove()

  def hasNext = !queue.isEmpty
}

object Event extends Enumeration {
  type Event = Value
  val WallHit, PaddleHit, BrickHit = Value
}
