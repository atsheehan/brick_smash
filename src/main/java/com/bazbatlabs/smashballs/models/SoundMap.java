package com.bazbatlabs.smashballs.models;

import java.util.*;
import java.io.*;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;

public final class SoundMap {

    private static final int MAX_STREAMS = 20;

    private final Map<Sound, Integer> map;
    private final SoundPool soundPool;

    public SoundMap(Resources resources) {
        this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        this.map = new HashMap<Sound, Integer>();

        for (Sound sound : Sound.values()) {

            int id = -1;
            try {
                id = soundPool.load(resources.getAssets().openFd("sounds/" + sound.filename()), 1);

            } catch (IOException e) {
                throw new RuntimeException("Failed to load asset: " + sound.filename(), e);
            }

            map.put(sound, id);
        }
    }


    public static enum Sound {
        BLIP("blip.ogg"),
        BLIP_LOW("blip_low.ogg");

        private final String filename;

        Sound(String filename) {
            this.filename = filename;
        }

        public String filename() { return filename; }
    }

    public void play(Sound sound) {
        soundPool.play(map.get(sound), 1, 1, 0, 0, 1);
    }
}
