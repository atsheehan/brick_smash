package com.bazbatlabs.smashballs.models;

public final class World {

    private final Paddle paddle;
    private final Ball ball;

    public World() {
        this.paddle = new Paddle(100f, 500f, 30f);
        this.ball = new Ball(new Vec2(200f, 200f));
    }

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
