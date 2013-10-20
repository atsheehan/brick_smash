package com.bazbatlabs.bricksmash.models;

public final class Image {

    public final int textureId;

    public final float x;
    public final float y;
    public final float w;
    public final float h;

    public Image(float x, float y, float w, float h, Texture texture) {

        float textureWidth = (float)texture.width;
        float textureHeight = (float)texture.height;

        this.x = x / textureWidth;
        this.y = y / textureHeight;
        this.w = w / textureWidth;
        this.h = h / textureHeight;

        this.textureId = texture.id;
    }
}
