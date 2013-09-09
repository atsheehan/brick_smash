package com.bazbatlabs.smashballs.controllers;

import android.view.KeyEvent;

import tv.ouya.console.api.OuyaController;

import com.bazbatlabs.smashballs.models.*;
import com.bazbatlabs.smashballs.views.MenuView;

public final class MenuController implements Controller {

    private final Menu menu;
    private final MenuView view;
    private final Artist artist;
    private final WorldController worldController;

    public MenuController(Artist artist, ImageMap images, SoundMap sounds, WorldController worldController) {
        this.menu = new Menu();
        this.view = new MenuView(this.menu, images, sounds, artist);
        this.artist = artist;
        this.worldController = worldController;
    }

    @Override
    public void draw(int screenWidth, int screenHeight) {
        view.draw();
    }

    @Override
    public Controller update() {
        menu.update();

        if (menu.isReadyToStartGame()) {
            return worldController;
        } else {
            return this;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = true;

        switch (keyCode) {
        case OuyaController.BUTTON_DPAD_UP:
            menu.previousEntry();
            break;

        case OuyaController.BUTTON_DPAD_DOWN:
            menu.nextEntry();
            break;

        case OuyaController.BUTTON_O:
            menu.select();
            break;

        default:
            handled = false;
            break;
        }

        return handled;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void changeSurface(int width, int height) {
        artist.changeSurface(width, height);
    }
}
