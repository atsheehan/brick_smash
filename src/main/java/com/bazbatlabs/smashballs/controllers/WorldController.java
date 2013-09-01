package com.bazbatlabs.smashballs.controllers;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;
import android.content.res.Resources;

import tv.ouya.console.api.OuyaController;

import com.bazbatlabs.smashballs.models.*;

public final class WorldController implements Controller {

    private final World world;
    private final Artist artist;

    public WorldController(Resources resources, Artist artist) {
        this.world = new World();
        this.artist = artist;
    }

    @Override
    public void draw(int screenWidth, int screenHeight) {
        artist.startDrawing();
        world.draw(artist);
        artist.finishDrawing(screenWidth, screenHeight);
    }

    @Override
    public Controller update() {
        world.update();
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = true;

        switch (keyCode) {
        case OuyaController.BUTTON_DPAD_LEFT:
            world.startAcceleratingPaddle(Direction.LEFT);
            break;

        case OuyaController.BUTTON_DPAD_RIGHT:
            world.startAcceleratingPaddle(Direction.RIGHT);
            break;

        default:
            handled = false;
            break;
        }

        return handled;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = true;

        switch (keyCode) {
        case OuyaController.BUTTON_DPAD_LEFT:
            world.stopAcceleratingPaddle(Direction.LEFT);
            break;

        case OuyaController.BUTTON_DPAD_RIGHT:
            world.stopAcceleratingPaddle(Direction.RIGHT);
            break;

        case OuyaController.BUTTON_O:
            world.kickstartBall();
            break;

        default:
            handled = false;
            break;
        }

        return handled;
    }
}
