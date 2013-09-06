package com.bazbatlabs.smashballs.models;

import java.util.*;

public final class World {

    public static final float WIDTH = 400.0f;
    public static final float HEIGHT = 300.0f;

    private static final int INITIAL_CHANCES = 30;
    private static final int BRICKS_PER_ROW = 16;
    private static final float BRICK_WIDTH = WIDTH / BRICKS_PER_ROW;
    private static final float BRICK_HEIGHT = BRICK_WIDTH / 3.0f;

    private final Rect bounds;
    private final Paddle paddle;
    private final Ball ball;
    private final List<Wall> walls;
    private final List<Brick> bricks;
    private final WorldEvents events;
    private State state;
    private int chancesRemaining;

    public World(WorldEvents events) {
        Vec2 origin = Vec2.ZERO;
        Vec2 size = new Vec2(WIDTH, HEIGHT);

        this.bounds = new Rect(origin, size);
        this.events = events;

        this.chancesRemaining = INITIAL_CHANCES;

        this.walls = new ArrayList<Wall>();

        this.walls.add(new Wall(new Rect(origin, new Vec2(0.0f, size.y)), events));
        this.walls.add(new Wall(new Rect(new Vec2(origin.x, origin.y + size.y),
                                         new Vec2(size.x, 0.0f)), events));
        this.walls.add(new Wall(new Rect(new Vec2(origin.x + size.x, origin.y),
                                         new Vec2(0.0f, size.y)), events));

        Vec2 brickSize = new Vec2(BRICK_WIDTH, BRICK_HEIGHT);

        this.bricks = new ArrayList<Brick>();
        for (int i = 0; i < BRICKS_PER_ROW; i++) {
            Vec2 pos = new Vec2(origin.x + (i * BRICK_WIDTH), 250.0f);
            this.bricks.add(new Brick(new Rect(pos, brickSize), Brick.Type.TOUGH, events));
        }

        this.paddle = new Paddle(bounds, 50f, events);
        this.ball = new Ball(paddle.center(), this);
        this.state = State.NORMAL;
    }

    public float floor() { return bounds.bottom(); }
    public Rect bounds() { return bounds; }

    public Paddle paddle() { return paddle; }
    public Ball ball() { return ball; }
    public List<Brick> bricks() { return bricks; }

    public void update() {
        if (state == State.NORMAL) {
            paddle.update();
            ball.update();

            for (Brick brick : bricks) {
                brick.update();
            }

            clearDestroyedBricks();

            if (bricks.isEmpty()) {
                state = State.CLEARED;
            }

            if (ball.isLost()) {

                chancesRemaining--;

                if (chancesRemaining > 0) {
                    ball.reset(paddle.center());
                } else {
                    state = State.GAME_OVER;
                }
            }
        }
    }

    private void clearDestroyedBricks() {
        ListIterator<Brick> iter = bricks.listIterator();
        while (iter.hasNext()) {
            if (iter.next().isDestroyed()) {
                iter.remove();
            }
        }
    }

    public List<Collidable> collidables() {
        List<Collidable> collidables =  new ArrayList<Collidable>();

        collidables.addAll(walls);
        collidables.addAll(bricks);
        collidables.add(paddle);

        return collidables;
    }

    public void startAcceleratingPaddle(Direction direction) {
        paddle.startAccelerating(direction);
    }

    public void stopAcceleratingPaddle(Direction direction) {
        paddle.stopAccelerating(direction);
    }

    public void kickstartBall() {
        Vec2 vel = paddle.vel();
        float angle = 0.0f;

        if (vel.x > 0.0f) {
            angle = (float)Math.PI / 4f;
        } else if (vel.x < 0.0f) {
            angle = 3f * (float)Math.PI / 4f;
        } else {
            angle = (float)Math.PI / 2f;
        }

        ball.kickstart(angle);
    }

    public boolean isGameOver() { return state == State.GAME_OVER; }
    public boolean isCleared() { return state == State.CLEARED; }

    private enum State {
        NORMAL, CLEARED, GAME_OVER
    }
}
