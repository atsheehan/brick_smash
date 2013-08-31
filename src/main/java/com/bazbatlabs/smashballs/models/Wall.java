package com.bazbatlabs.smashballs.models;

public final class Wall implements Collidable {

    private final Rect bounds;

    public Wall(Rect bounds) {
        this.bounds = bounds;
    }

    public Rect bounds() { return bounds; }
    public void hit() {}
}
