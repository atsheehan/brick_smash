package com.bazbatlabs.bricksmash.models;

import java.util.Queue;
import java.util.LinkedList;

public final class WorldEvents {

    private final Queue<Event> queue;

    public WorldEvents() {
        this.queue = new LinkedList<Event>();
    }

    public void enqueue(Event event) {
        queue.add(event);
    }

    public Event dequeue() {
        return queue.remove();
    }

    public boolean hasNext() {
        return !queue.isEmpty();
    }

    public static enum Event {
        WALL_HIT,
        PADDLE_HIT,
        BRICK_HIT
    }
}
