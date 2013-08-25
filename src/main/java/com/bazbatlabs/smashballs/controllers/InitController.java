package com.bazbatlabs.smashballs.controllers;

import java.io.*;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public final class InitController implements Controller {

    private AssetManager assets;
    private boolean finished;


    public InitController(AssetManager assets) {
        this.assets = assets;
        this.finished = false;
    }

    @Override
    public void draw(GL10 gl, int screenWidth, int screenHeight) {
        if (!finished) {
            finished = true;
        }
    }

    @Override
    public Controller update() {
        return new WorldController();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { return false; }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) { return false; }
}
