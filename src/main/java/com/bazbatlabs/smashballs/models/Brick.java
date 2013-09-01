package com.bazbatlabs.smashballs.models;

public final class Brick implements Collidable {

    private final Rect bounds;
    private State state;

    public Brick(Rect bounds) {
        this.bounds = bounds;
        this.state = State.NORMAL;
    }

    public Rect bounds() { return bounds; }

    public void hit() {
        state = State.DESTROYED;
    }

    public void update() {}

    public boolean isDestroyed() {
        return state == State.DESTROYED;
    }

    public enum State {
        NORMAL, DESTROYED
    }
}
