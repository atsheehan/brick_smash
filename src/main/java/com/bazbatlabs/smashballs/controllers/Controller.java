package com.bazbatlabs.smashballs.controllers;

import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;

public interface Controller {
    public void draw(int screenWidth, int screenHeight);
    public Controller update();
    public boolean onKeyDown(int keyCode, KeyEvent event);
    public boolean onKeyUp(int keyCode, KeyEvent event);
    public void changeSurface(int width, int height);
}
