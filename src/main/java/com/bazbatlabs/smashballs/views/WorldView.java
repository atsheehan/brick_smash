package com.bazbatlabs.smashballs.views;

import com.bazbatlabs.smashballs.models.*;

public final class WorldView {

    public static final float BORDER_WIDTH = 6f;

    private final World world;
    private final ImageMap images;
    private final Artist artist;

    private final Rect leftBorder;
    private final Rect rightBorder;
    private final Rect topBorder;
    private final Rect leftCap;
    private final Rect rightCap;


    public WorldView(World world, ImageMap images, Artist artist) {
        this.world = world;
        this.images = images;
        this.artist = artist;

        Rect bounds = world.bounds();

        this.leftBorder =
            new Rect(new Vec2(bounds.left() - BORDER_WIDTH, bounds.bottom()),
                     new Vec2(BORDER_WIDTH, bounds.size.y));

        this.leftCap =
            new Rect(new Vec2(bounds.left() - BORDER_WIDTH, bounds.top()),
                     new Vec2(BORDER_WIDTH, BORDER_WIDTH));

        this.rightBorder =
            new Rect(new Vec2(bounds.right(), bounds.bottom()),
                     new Vec2(BORDER_WIDTH, bounds.size.y));

        this.rightCap =
            new Rect(new Vec2(bounds.right(), bounds.top()),
                     new Vec2(BORDER_WIDTH, BORDER_WIDTH));

        this.topBorder =
            new Rect(new Vec2(bounds.left(), bounds.top()),
                     new Vec2(bounds.size.x, BORDER_WIDTH));
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

        artist.drawImage(leftBorder, images.get("BORDER_SIDE"));
        artist.drawImage(rightBorder, images.get("BORDER_SIDE"));
        artist.drawImage(topBorder, images.get("BORDER_TOP"));

        artist.drawImage(leftCap, images.get("BORDER_CORNER"));
        artist.drawImage(rightCap, images.get("BORDER_CORNER"));


        artist.finishDrawing();
    }
}
