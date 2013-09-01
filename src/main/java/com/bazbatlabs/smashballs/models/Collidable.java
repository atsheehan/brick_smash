package com.bazbatlabs.smashballs.models;

public interface Collidable {
    Rect bounds();
    void hit();
    Vec2 deflect(Vec2 vel, Vec2 collision, Axis axis);
}
