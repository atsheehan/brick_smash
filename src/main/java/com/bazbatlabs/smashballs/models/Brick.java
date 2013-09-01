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

    public Vec2 deflect(Vec2 vel, Vec2 collision, Axis axis) {
        switch (axis) {
        case X: return new Vec2(-vel.x, vel.y);
        case Y: return new Vec2(vel.x, -vel.y);
        default: return Vec2.ZERO;
        }
    }

    private enum State {
        NORMAL, DESTROYED
    }
}
