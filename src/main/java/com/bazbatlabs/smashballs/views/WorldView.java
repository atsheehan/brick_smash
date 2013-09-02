package com.bazbatlabs.smashballs.views;

import com.bazbatlabs.smashballs.models.*;

public final class WorldView {

    private final World world;
    private final ImageMap images;
    private final Artist artist;

    public WorldView(World world, ImageMap images, Artist artist) {
        this.world = world;
        this.images = images;
        this.artist = artist;
    }

    public void draw() {
        Paddle paddle = world.paddle();
        Ball ball = world.ball();

        artist.startDrawing();

        artist.drawImage(paddle.pos(), paddle.size(), images.get("PADDLE"));
        artist.drawImage(ball.pos(), ball.size(), images.get("BALL"), Color.GREEN);

        Image[] brickImages = new Image[] {
            images.get("BRICK"),
            images.get("BRICK_1"),
            images.get("BRICK_2"),
            images.get("BRICK_3"),
            images.get("BRICK_4")
        };

        for (Brick brick : world.bricks()) {
            if (brick.isBreaking()) {
                int index = brick.stateCounter() / 4;
                artist.drawImage(brick.bounds(), brickImages[index], Color.RED);

            } else {
                artist.drawImage(brick.bounds(), brickImages[0], Color.RED);
            }
        }

        artist.finishDrawing();
    }
}
