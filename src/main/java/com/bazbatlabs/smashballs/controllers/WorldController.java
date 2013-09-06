package com.bazbatlabs.smashballs.controllers;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;

import tv.ouya.console.api.OuyaController;

import com.bazbatlabs.smashballs.models.*;
import com.bazbatlabs.smashballs.views.WorldView;

public final class WorldController implements Controller {

    private final World world;
    private final WorldView view;
    private final Artist artist;

    public WorldController(Artist artist, ImageMap images, SoundMap sounds) {
        WorldEvents events = new WorldEvents();

        this.world = new World(events);
        this.view = new WorldView(this.world, events, images, sounds, artist);
        this.artist = artist;
    }

    @Override
    public void draw(int screenWidth, int screenHeight) {
        view.draw();
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

    @Override
    public void changeSurface(int width, int height) {
        artist.changeSurface(width, height);
    }
}
