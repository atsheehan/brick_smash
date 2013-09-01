package com.bazbatlabs.smashballs.models;

public final class Wall implements Collidable {

    private final Rect bounds;

    public Wall(Rect bounds) {
        this.bounds = bounds;
    }

    public Rect bounds() { return bounds; }
    public void hit() {}

    public Vec2 deflect(Vec2 vel, Vec2 collision, Axis axis) {
        switch (axis) {
        case X: return new Vec2(-vel.x, vel.y);
        case Y: return new Vec2(vel.x, -vel.y);
        default: return Vec2.ZERO;
        }
    }
}
