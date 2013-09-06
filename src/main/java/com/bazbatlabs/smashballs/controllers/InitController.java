package com.bazbatlabs.smashballs.controllers;

import java.io.*;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;

import com.bazbatlabs.smashballs.models.Artist;
import com.bazbatlabs.smashballs.models.ImageMap;
import com.bazbatlabs.smashballs.models.SoundMap;

public final class InitController implements Controller {

    private final Resources resources;
    private Artist artist;
    private ImageMap images;
    private SoundMap sounds;
    private boolean finished;

    public InitController(Resources resources) {
        this.resources = resources;
        this.finished = false;

        // Can't instantiate Artist in here since it needs to happen when there
        // is an Android context available (in the draw, update, or changeSurface
        // methods).
        this.artist = null;
        this.images = null;
        this.sounds = null;
    }

    @Override
    public void draw(int screenWidth, int screenHeight) {
        if (!finished) {
            if (artist == null) {
                artist = new Artist(resources, screenWidth, screenHeight);
            }

            if (images == null) {
                images = new ImageMap(resources);
            }

            if (sounds == null) {
                sounds = new SoundMap(resources);
            }

            finished = true;
        }
    }

    @Override
    public Controller update() {
        if (finished) {
            return new WorldController(artist, images, sounds);
        } else {
            return this;
        }
    }

    @Override
    public void changeSurface(int width, int height) {
        if (artist != null) {
            artist.changeSurface(width, height);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { return false; }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) { return false; }
}
