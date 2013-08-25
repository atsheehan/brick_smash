package com.bazbatlabs.smashballs.models;

public final class Ball {

    private static final float RADIUS = 5.0f;
    private static final float DIAMETER = RADIUS + RADIUS;
    private static final float SPEED = 5.0f;

    private final Vec2 size;
    private final Vec2 halfSize;

    private Vec2 center;
    private Vec2 vel;

    public Ball(Vec2 initialCenter) {
        this.center = initialCenter;
        this.vel = Vec2.fromAngle((float)Math.PI / 4f).scale(SPEED);

        this.size = new Vec2(DIAMETER, DIAMETER);
        this.halfSize = new Vec2(RADIUS, RADIUS);
    }

    public Vec2 pos() { return center.subtract(halfSize); }
    public Vec2 size() { return size; }

    public void update() {
        center = center.add(vel);
    }

    public void kickstart(float angleInRadians) {
        vel = Vec2.fromAngle(angleInRadians).scale(SPEED);
    }
}
