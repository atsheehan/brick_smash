package com.bazbatlabs.bricksmash.models;

public final class Brick implements Collidable {

    private static final int BREAKING_DURATION = 19;

    private final WorldEvents events;

    private final Rect bounds;
    private State state;
    private int stateCounter;
    private int durability;

    public Brick(Rect bounds, Type type, WorldEvents events) {

        this.events = events;

        this.bounds = bounds;
        this.state = State.NORMAL;
        this.stateCounter = 0;

        if (type == Type.TOUGH) {
            this.durability = 2;
        } else {
            this.durability = 1;
        }
    }

    public boolean isActive() { return state == State.NORMAL; }
    public boolean isDestroyed() { return state == State.DESTROYED; }
    public boolean isBreaking() { return state == State.BREAKING; }
    public int stateCounter() { return stateCounter; }

    public Rect bounds() { return bounds; }

    public void hit() {
        events.enqueue(WorldEvents.Event.BRICK_HIT);

        if (state == State.NORMAL) {
            durability--;

            if (durability <= 0) {
                state = State.BREAKING;
            }
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
        case X: return new Vec2(-vel.x(), vel.y());
        case Y: return new Vec2(vel.x(), -vel.y());
        default: return Vec2.Zero();
        }
    }

    private enum State {
        NORMAL, BREAKING, DESTROYED
    }

    public enum Type {
        NORMAL, TOUGH
    }
}
