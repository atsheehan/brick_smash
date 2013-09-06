package com.bazbatlabs.smashballs.models;

public final class Wall implements Collidable {

    private final Rect bounds;
    private final WorldEvents events;

    public Wall(Rect bounds, WorldEvents events) {
        this.bounds = bounds;
        this.events = events;
    }

    public Rect bounds() { return bounds; }

    public void hit() {
        events.enqueue(WorldEvents.Event.WALL_HIT);
    }

    public Vec2 deflect(Vec2 vel, Vec2 collision, Axis axis) {
        switch (axis) {
        case X: return new Vec2(-vel.x, vel.y);
        case Y: return new Vec2(vel.x, -vel.y);
        default: return Vec2.ZERO;
        }
    }

    public boolean isActive() { return true; }
}
