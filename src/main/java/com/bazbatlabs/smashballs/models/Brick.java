package com.bazbatlabs.smashballs.models;

public final class Brick implements Collidable {

    private static final int BREAKING_DURATION = 19;

    private final Rect bounds;
    private State state;
    private int stateCounter;

    public Brick(Rect bounds) {
        this.bounds = bounds;
        this.state = State.NORMAL;
        this.stateCounter = 0;
    }

    public boolean isActive() { return state == State.NORMAL; }
    public boolean isDestroyed() { return state == State.DESTROYED; }
    public boolean isBreaking() { return state == State.BREAKING; }
    public int stateCounter() { return stateCounter; }

    public Rect bounds() { return bounds; }

    public void hit() {
        if (state == State.NORMAL) {
            state = State.BREAKING;
        }
    }

    public void update() {
        if (state == State.BREAKING) {
            stateCounter++;

            if (stateCounter > BREAKING_DURATION) {
                state = State.DESTROYED;
            }
        }
    }

    public Vec2 deflect(Vec2 vel, Vec2 collision, Axis axis) {
        switch (axis) {
        case X: return new Vec2(-vel.x, vel.y);
        case Y: return new Vec2(vel.x, -vel.y);
        default: return Vec2.ZERO;
        }
    }

    private enum State {
        NORMAL, BREAKING, DESTROYED
    }
}
