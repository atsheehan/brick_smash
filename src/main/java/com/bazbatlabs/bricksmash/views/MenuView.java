package com.bazbatlabs.bricksmash.views;

import com.bazbatlabs.bricksmash.models.*;

public final class MenuView {

    private static final int CYCLE_LENGTH = 200;
    private static final float CYCLE_COEFFICIENT = (float)(2 * Math.PI) / (float)CYCLE_LENGTH;

    private final Menu menu;

    private final ImageMap images;
    private final SoundMap sounds;
    private final Artist artist;

    private float r;
    private float g;
    private float b;

    private int counter;

    public MenuView(Menu menu, ImageMap images, SoundMap sounds, Artist artist) {

        this.menu = menu;

        this.images = images;
        this.sounds = sounds;
        this.artist = artist;

        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;

        this.counter = 0;
    }

    public void draw() {
        artist.startDrawing();

        float radians = CYCLE_COEFFICIENT * (counter % CYCLE_LENGTH);

        b = r = (float)Math.abs(Math.sin(radians));
        g = (float)Math.abs(Math.cos(radians));

        Color color = new Color(r, g, b, 1f);

        artist.drawImage(new Vec2(100f, 200f), new Vec2(200f, 20f), images.get("TITLE"), color);

        artist.finishDrawing();

        counter++;
    }
}
