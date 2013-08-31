package com.bazbatlabs.smashballs.models;

public final class Brick implements Collidable {

    private static final float WIDTH = 20.0f;
    private static final float HEIGHT = 4.0f;

    private final Rect bounds;
    private State state;

    public Brick(Vec2 pos) {
        Vec2 size = new Vec2(WIDTH, HEIGHT);
        this.bounds = new Rect(pos, size);
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
