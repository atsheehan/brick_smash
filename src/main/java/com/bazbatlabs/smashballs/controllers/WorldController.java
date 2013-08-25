package com.bazbatlabs.smashballs.controllers;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;

import tv.ouya.console.api.OuyaController;

import com.bazbatlabs.smashballs.models.*;

public final class WorldController implements Controller {

    private final World world;
    private final Renderer renderer;

    public WorldController() {
        this.world = new World();
        this.renderer = new Renderer();
    }

    @Override
    public void draw(GL10 gl, int screenWidth, int screenHeight) {
        renderer.startDrawing(gl, 0);
        world.draw(renderer);
        renderer.finishDrawing(gl, screenWidth, screenHeight);
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

        default:
            handled = false;
            break;
        }

        return handled;
    }
}
