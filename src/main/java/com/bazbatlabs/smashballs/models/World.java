package com.bazbatlabs.smashballs.models;

public final class World {

    private final Paddle paddle;

    public World() {
        this.paddle = new Paddle(100f, 500f, 30f);
    }

    public void update() {
        paddle.update();
    }

    public void draw(Renderer renderer) {
        renderer.drawRect(paddle.pos(), paddle.size(), Color.RED);
    }

    public void startAcceleratingPaddle(Direction direction) {
        paddle.startAccelerating(direction);
    }

    public void stopAcceleratingPaddle(Direction direction) {
        paddle.stopAccelerating(direction);
    }
}
