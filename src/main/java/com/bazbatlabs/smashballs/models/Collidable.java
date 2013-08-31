package com.bazbatlabs.smashballs.models;

public interface Collidable {
    Rect bounds();
    void hit();
}
