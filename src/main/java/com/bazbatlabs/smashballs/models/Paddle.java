package com.bazbatlabs.smashballs.models;

public final class Paddle {

    private static final float ACCELERATION = 0.2f;
    private static final float WIDTH = 50.0f;
    private static final float HEIGHT = 10.0f;

    private final float leftWall;
    private final float rightWall;

    private final Vec2 size;

    private Vec2 pos;
    private Vec2 vel;

    private boolean acceleratingLeft;
    private boolean acceleratingRight;


    public Paddle(float leftWall, float rightWall, float startY) {

        this.acceleratingLeft = false;
        this.acceleratingRight = false;

        this.leftWall = leftWall;
        this.rightWall = rightWall;

        this.size = new Vec2(WIDTH, HEIGHT);

        float startX = leftWall + (rightWall - leftWall - this.size.x) / 2f;
        this.pos = new Vec2(startX, startY);

        this.vel = Vec2.ZERO;
    }

    public Vec2 pos() { return pos; }
    public Vec2 size() { return size; }

    public void update() {

        float accelX = 0.0f;
        if (acceleratingLeft) { accelX -= ACCELERATION; }
        if (acceleratingRight) { accelX += ACCELERATION; }

        Vec2 accel = new Vec2(accelX, 0.0f);

        vel = vel.add(accel);
        pos = pos.add(vel);
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
