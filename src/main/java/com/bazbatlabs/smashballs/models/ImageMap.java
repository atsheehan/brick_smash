package com.bazbatlabs.smashballs.models;

import java.util.Map;
import java.util.HashMap;

import android.content.res.Resources;
import com.bazbatlabs.smashballs.R;

public final class ImageMap {

    private final Map<String, Image> images;

    public ImageMap(Resources resources) {

        Texture texture = TextureLoader.load(R.raw.textures, resources);
        if (texture == null) {
            throw new RuntimeException("Failed to load texture. Check log for details.");
        }

        this.images = new HashMap<String, Image>();
        this.images.put("BRICK", new Image(0f, 0f, 40f, 12f, texture));
        this.images.put("BRICK_1", new Image(0f, 13f, 40f, 12f, texture));
        this.images.put("BRICK_2", new Image(0f, 26f, 40f, 12f, texture));
        this.images.put("BRICK_3", new Image(0f, 39f, 40f, 12f, texture));
        this.images.put("BRICK_4", new Image(0f, 52f, 40f, 12f, texture));
        this.images.put("BALL", new Image(0f, 156f, 8f, 8f, texture));
        this.images.put("PADDLE", new Image(0f, 188f, 48f, 12f, texture));

        this.images.put("GAME_OVER", new Image(74f, 65f, 128f, 16f, texture));
        this.images.put("CLEARED", new Image(74f, 82f, 104f, 16f, texture));

        this.images.put("BORDER_CORNER", new Image(41f, 0f, 6f, 6f, texture));
        this.images.put("BORDER_TOP", new Image(41f, 7f, 6f, 6f, texture));
        this.images.put("BORDER_SIDE", new Image(41f, 14f, 6f, 6f, texture));

        this.images.put("TITLE", new Image(2f, 120f, 187f, 16f, texture));
    }

    public Image get(String name) {
        return images.get(name);
    }
}
