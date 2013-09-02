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
        this.images.put("RED_BRICK", new Image(0f, 0f, 40f, 12f, texture));
        this.images.put("BLUE_BRICK", new Image(41f, 0f, 40f, 12f, texture));
        this.images.put("GREEN_BRICK", new Image(82f, 0f, 40f, 12f, texture));
        this.images.put("GREEN_BRICK_1", new Image(82f, 13f, 40f, 12f, texture));
        this.images.put("GREEN_BRICK_2", new Image(82f, 26f, 40f, 12f, texture));
        this.images.put("GREEN_BRICK_3", new Image(82f, 39f, 40f, 12f, texture));
        this.images.put("GREEN_BRICK_4", new Image(82f, 52f, 40f, 12f, texture));
        this.images.put("BALL", new Image(0f, 156f, 8f, 8f, texture));
        this.images.put("PADDLE", new Image(0f, 188f, 48f, 12f, texture));
    }

    public Image get(String name) {
        return images.get(name);
    }
}
