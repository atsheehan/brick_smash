package com.bazbatlabs.smashballs.models;

public final class World {

    private static final float WIDTH = 400.0f;
    private static final float HEIGHT = 300.0f;

    private final Paddle paddle;
    private final Ball ball;
    private final Rect bounds;

    public World() {
        this.bounds = new Rect(new Vec2(100.0f, 20.0f), new Vec2(WIDTH, HEIGHT));
        this.paddle = new Paddle(this.bounds, 50f);
        this.ball = new Ball(new Vec2(200f, 200f), this);
    }

    public Rect bounds() { return bounds; }

    public void update() {
        paddle.update();
        ball.update();
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
