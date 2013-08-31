package com.bazbatlabs.smashballs.models;

import java.util.AbstractMap.SimpleEntry;

public final class Ball {

    private static final float RADIUS = 5.0f;
    private static final float DIAMETER = RADIUS + RADIUS;
    private static final float SPEED = 5.0f;

    private final Vec2 size;
    private final Vec2 halfSize;

    private World world;
    private Vec2 center;
    private Vec2 vel;

    public Ball(Vec2 initialCenter, World world) {
        this.world = world;

        this.center = initialCenter;
        this.vel = Vec2.fromAngle((float)Math.PI / 4f).scale(SPEED);

        this.size = new Vec2(DIAMETER, DIAMETER);
        this.halfSize = new Vec2(RADIUS, RADIUS);
    }

    public Vec2 pos() { return center.subtract(halfSize); }
    public Vec2 size() { return size; }

    public void update() {
        move(vel);
    }

    private void move(Vec2 effectiveVel) {
        Vec2 newCenter = center.add(effectiveVel);

        Rect bounds = world.bounds();

        boolean flipX = false;
        float minDistance = Float.MAX_VALUE;

        if (effectiveVel.x > 0.0f) {
            if (newCenter.x > bounds.right()) {
                float distanceToCollision =
                    (bounds.right() - center.x) / (newCenter.x - center.x);

                if (distanceToCollision < minDistance) {
                    minDistance = distanceToCollision;
                    flipX = true;
                }
            }
        }

        if (effectiveVel.x < 0.0f) {
            if (newCenter.x < bounds.left()) {
                float distanceToCollision =
                    (bounds.left() - center.x) / (newCenter.x - center.x);

                if (distanceToCollision < minDistance) {
                    minDistance = distanceToCollision;
                    flipX = true;
                }
            }
        }

        if (effectiveVel.y > 0.0f) {
            if (newCenter.y > bounds.top()) {
                float distanceToCollision =
                    (bounds.top() - center.y) / (newCenter.y - center.y);

                if (distanceToCollision < minDistance) {
                    minDistance = distanceToCollision;
                    flipX = false;
                }
            }
        }

        if (effectiveVel.y < 0.0f) {
            if (newCenter.y < bounds.bottom()) {
                float distanceToCollision =
                    (bounds.bottom() - center.y) / (newCenter.y - center.y);

                if (distanceToCollision < minDistance) {
                    minDistance = distanceToCollision;
                    flipX = false;
                }
            }
        }

        if (minDistance < 1.0f) {
            Vec2 actualVel = effectiveVel.scale(minDistance);
            Vec2 remainingVel = effectiveVel.subtract(actualVel);

            if (flipX) {
                vel = new Vec2(-vel.x, vel.y);
                remainingVel = new Vec2(-remainingVel.x, remainingVel.y);
            } else {
                vel = new Vec2(vel.x, -vel.y);
                remainingVel = new Vec2(remainingVel.x, -remainingVel.y);
            }

            center = center.add(actualVel);

            move(remainingVel);
        } else {
            center = newCenter;
        }
    }

    public void kickstart(float angleInRadians) {
        vel = Vec2.fromAngle(angleInRadians).scale(SPEED);
    }
}
