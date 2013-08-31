package com.bazbatlabs.smashballs.models;

public final class Paddle {

    private static final float FRICTION = 0.1f;
    private static final float ACCELERATION = 0.5f;
    private static final float WIDTH = 50.0f;
    private static final float HEIGHT = 10.0f;
    private static final float MAX_SPEED = 5.0f;

    private final Rect bounds;

    private final Vec2 size;

    private Vec2 pos;
    private Vec2 vel;

    private boolean acceleratingLeft;
    private boolean acceleratingRight;

    public Paddle(Rect bounds, float startY) {

        this.acceleratingLeft = false;
        this.acceleratingRight = false;

        this.bounds = bounds;

        this.size = new Vec2(WIDTH, HEIGHT);

        float startX =
            bounds.left() + (bounds.right() - bounds.left() - this.size.x) / 2f;

        this.pos = new Vec2(startX, startY);

        this.vel = Vec2.ZERO;
    }

    public Vec2 pos() { return pos; }
    public Vec2 size() { return size; }
    public Rect bounds() { return new Rect(pos, size); }

    public void update() {

        float accelX = 0.0f;
        if (acceleratingLeft) { accelX -= ACCELERATION; }
        if (acceleratingRight) { accelX += ACCELERATION; }

        Vec2 accel = new Vec2(accelX, 0.0f);

        vel = vel.add(accel).scale(1f - FRICTION);

        if (vel.x > MAX_SPEED) { vel = new Vec2(MAX_SPEED, 0.0f); }
        if (vel.x < -MAX_SPEED) { vel = new Vec2(-MAX_SPEED, 0.0f); }

        pos = pos.add(vel);

        if (pos.x + size.x > bounds.right()) {
            vel = Vec2.ZERO;
            pos = new Vec2(bounds.right() - size.x, pos.y);
        }

        if (pos.x < bounds.left()) {
            vel = Vec2.ZERO;
            pos = new Vec2(bounds.left(), pos.y);
        }
    }

    public void startAccelerating(Direction direction) {
        toggleAcceleration(direction, true);
    }

    public void stopAccelerating(Direction direction) {
        toggleAcceleration(direction, false);
    }

    private void toggleAcceleration(Direction direction, boolean start) {
        switch (direction) {
        case LEFT: acceleratingLeft = start; break;
        case RIGHT: acceleratingRight = start; break;
        default: break;
        }
    }
}
