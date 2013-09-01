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

    public void kickstart(float angleInRadians) {
        vel = Vec2.fromAngle(angleInRadians).scale(SPEED);
    }

    public void update() {
        move(vel);
    }

    private void move(Vec2 effectiveVel) {
        Vec2 newCenter = center.add(effectiveVel);

        // Find the closest collision amongst all of the collidable objects
        Collision collision = Collision.NONE;

        for (Collidable collidable : world.collidables()) {
            collision = nearestCollision(collidable, effectiveVel, newCenter, collision);
        }

        if (collision.distance < 1.0f) {
            Vec2 actualVel = effectiveVel.scale(collision.distance);
            Vec2 remainingVel = effectiveVel.subtract(actualVel);

            center = center.add(actualVel);

            // Reverse direction after a collision
            switch (collision.axis) {
            case X:
                vel = new Vec2(-vel.x, vel.y);
                remainingVel = new Vec2(-remainingVel.x, remainingVel.y);
                break;

            case Y:
                vel = new Vec2(vel.x, -vel.y);
                remainingVel = new Vec2(remainingVel.x, -remainingVel.y);
                break;
            }

            collision.object.hit();
            move(remainingVel);

        } else {
            center = newCenter;
        }
    }

    private Collision nearestCollision(Collidable collidable, Vec2 effectiveVel,
                                       Vec2 newCenter, Collision bestCollision) {

        Rect bounds = collidable.bounds();

        float top = bounds.top() + RADIUS;
        float bottom = bounds.bottom() - RADIUS;
        float left = bounds.left() - RADIUS;
        float right = bounds.right() + RADIUS;

        if (effectiveVel.y < 0.0f) {
            if (center.y > top && newCenter.y < top) {
                float distance = (top - center.y) / (newCenter.y - center.y);

                if (distance < bestCollision.distance) {
                    float xIntercept = center.x + (effectiveVel.x * distance);

                    if (xIntercept > left && xIntercept < right) {
                        bestCollision = new Collision(collidable, distance, Axis.Y);
                    }
                }
            }
        }

        if (effectiveVel.y > 0.0f) {
            if (center.y < bottom && newCenter.y > bottom) {
                float distance = (bottom - center.y) / (newCenter.y - center.y);

                if (distance < bestCollision.distance) {
                    float xIntercept = center.x + (effectiveVel.x * distance);

                    if (xIntercept > left && xIntercept < right) {
                        bestCollision = new Collision(collidable, distance, Axis.Y);
                    }
                }
            }
        }

        if (effectiveVel.x < 0.0f) {
            if (center.x > right && newCenter.x < right) {
                float distance = (right - center.x) / (newCenter.x - center.x);

                if (distance < bestCollision.distance) {
                    float yIntercept = center.y + (effectiveVel.y * distance);

                    if (yIntercept > bottom && yIntercept < top) {
                        bestCollision = new Collision(collidable, distance, Axis.X);
                    }
                }
            }
        }

        if (effectiveVel.x > 0.0f) {
            if (center.x < left && newCenter.x > left) {
                float distance = (left - center.x) / (newCenter.x - center.x);

                if (distance < bestCollision.distance) {
                    float yIntercept = center.y + (effectiveVel.y * distance);

                    if (yIntercept > bottom && yIntercept < top) {
                        bestCollision = new Collision(collidable, distance, Axis.X);
                    }
                }
            }
        }

        return bestCollision;
    }

    private static class Collision {
        public static final Collision NONE = new Collision(null, Float.MAX_VALUE, Axis.X);

        public final Collidable object;
        public final float distance;
        public final Axis axis;

        public Collision(Collidable object, float distance, Axis axis) {
            this.object = object;
            this.distance = distance;
            this.axis = axis;
        }
    }

    private enum Axis { X, Y }
}
