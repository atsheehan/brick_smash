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

    public InitController(Resources resources) {
        this.resources = resources;
        this.finished = false;
    }

    @Override
    public void draw(int screenWidth, int screenHeight) {
        if (!finished) {
            finished = true;
        }
    }

    @Override
    public Controller update() {
        return new WorldController(resources, new Artist(resources));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { return false; }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) { return false; }
}
