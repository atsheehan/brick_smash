package com.bazbatlabs.smashballs.models;

import java.util.*;

public final class World {

    private static final float WIDTH = 400.0f;
    private static final float HEIGHT = 300.0f;

    private final Paddle paddle;
    private final Ball ball;
    private final List<Wall> walls;

    public World() {
        Vec2 origin = new Vec2(100.0f, 20.0f);
        Vec2 size = new Vec2(WIDTH, HEIGHT);

        this.walls = new ArrayList<Wall>();

        walls.add(new Wall(new Rect(origin, new Vec2(size.x, 0.0f))));
        walls.add(new Wall(new Rect(origin, new Vec2(0.0f, size.y))));
        walls.add(new Wall(new Rect(new Vec2(origin.x, origin.y + size.y),
                                    new Vec2(size.x, 0.0f))));
        walls.add(new Wall(new Rect(new Vec2(origin.x + size.x, origin.y),
                                    new Vec2(0.0f, size.y))));

        this.paddle = new Paddle(new Rect(origin, size), 50f);
        this.ball = new Ball(new Vec2(200f, 200f), this);
    }

    public Paddle paddle() { return paddle; }

    public void update() {
        paddle.update();
        ball.update();
    }

    public List<Collidable> collidables() {
        List<Collidable> collidables =  new ArrayList<Collidable>();
        collidables.addAll(walls);
        collidables.add(paddle);

        return collidables;

    }

    public void draw(Renderer renderer) {
        renderer.drawRect(paddle.pos(), paddle.size(), Color.RED);
        renderer.drawRect(ball.pos(), ball.size(), Color.BLUE);
    }

    public void startAcceleratingPaddle(Direction direction) {
        paddle.startAccelerating(direction);
    }

    public void stopAcceleratingPaddle(Direction direction) {
        paddle.stopAccelerating(direction);
    }
}
