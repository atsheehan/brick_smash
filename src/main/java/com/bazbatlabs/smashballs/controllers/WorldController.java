package com.bazbatlabs.smashballs.controllers;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;

public final class WorldController implements Controller {

    public WorldController() {}

    @Override
    public void draw(GL10 gl, int screenWidth, int screenHeight) {}

    @Override
    public Controller update() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}
