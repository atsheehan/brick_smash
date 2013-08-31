package com.bazbatlabs.smashballs.models;

public final class Brick implements Collidable {

    private static final float WIDTH = 20.0f;
    private static final float HEIGHT = 4.0f;

    private final Rect bounds;

    public Brick(Vec2 pos) {
        Vec2 size = new Vec2(WIDTH, HEIGHT);
        this.bounds = new Rect(pos, size);
    }

    public Rect bounds() { return bounds; }
    public void hit() {}

}
