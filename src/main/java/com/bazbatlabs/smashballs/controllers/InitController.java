package com.bazbatlabs.smashballs.controllers;

import java.io.*;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;

import com.bazbatlabs.smashballs.models.Artist;

public final class InitController implements Controller {

    private final Resources resources;
    private boolean finished;
    private Artist artist;

    public InitController(Resources resources) {
        this.resources = resources;
        this.finished = false;

        // Can't instantiate Artist in here since it needs to happen when there
        // is an Android context available (in the draw, update, or changeSurface
        // methods).
    }

    @Override
    public void draw(int screenWidth, int screenHeight) {
        if (!finished) {
            finished = true;
        }
    }

    @Override
    public Controller update() {
        return new WorldController(resources, artist());
    }

    @Override
    public void changeSurface(int width, int height) {
        artist().changeSurface(width, height);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { return false; }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) { return false; }


    private Artist artist() {
        if (artist == null) {
            artist = new Artist(resources);
        }

        return artist;
    }
}
