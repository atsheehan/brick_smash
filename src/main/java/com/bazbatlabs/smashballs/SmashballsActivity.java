package com.bazbatlabs.smashballs;

import android.util.Log;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.media.AudioManager;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import tv.ouya.console.api.OuyaController;

import com.bazbatlabs.smashballs.controllers.*;

public final class SmashballsActivity extends Activity implements Renderer  {

    private static final String TAG = "SmashballsActivity";

    private static final int LOG_FPS_INTERVAL = 100;

    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    private static final int UPDATES_PER_SECOND = 60;

    private static final long UPDATE_DELTA =
        NANOSECONDS_PER_SECOND / UPDATES_PER_SECOND;

    private long fpsStart;
    private long fpsFrames;

    private long ticksLastFrame;
    private long tickCounter;

    private Controller controller;
    private GLSurfaceView view;
    private boolean rendererSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rendererSet = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        view = new GLSurfaceView(this);
        view.setRenderer(this);
        rendererSet = true;

        setContentView(view);

        controller = new InitController(getAssets());
        OuyaController.init(this);

        ticksLastFrame = System.nanoTime();
        tickCounter = 0;

        fpsStart = SystemClock.elapsedRealtime();
        fpsFrames = 0;
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        long currentTime = System.nanoTime();
        tickCounter += currentTime - ticksLastFrame;
        ticksLastFrame = currentTime;

        controller.draw(gl, view.getWidth(), view.getHeight());

        while (tickCounter > UPDATE_DELTA) {
            controller = controller.update();
            tickCounter -= UPDATE_DELTA;
        }

        logFramerate();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {}

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {}

    @Override
    protected void onPause() {
        super.onPause();

        if (rendererSet) {
            view.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rendererSet) {
            view.onResume();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isOuyaKey(keyCode)) {
            return controller.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isOuyaKey(keyCode)) {
            return controller.onKeyUp(keyCode, event);
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    private boolean isOuyaKey(int keyCode) {
        switch (keyCode) {
        case OuyaController.BUTTON_O:
        case OuyaController.BUTTON_U:
        case OuyaController.BUTTON_Y:
        case OuyaController.BUTTON_A:
        case OuyaController.BUTTON_R1:
        case OuyaController.BUTTON_R2:
        case OuyaController.BUTTON_R3:
        case OuyaController.BUTTON_L1:
        case OuyaController.BUTTON_L2:
        case OuyaController.BUTTON_L3:
        case OuyaController.BUTTON_DPAD_DOWN:
        case OuyaController.BUTTON_DPAD_LEFT:
        case OuyaController.BUTTON_DPAD_RIGHT:
        case OuyaController.BUTTON_DPAD_UP:
        case OuyaController.BUTTON_MENU:
            return true;

        default:
            return false;
        }
    }

    private void logFramerate() {
        fpsFrames++;

        if (fpsFrames % LOG_FPS_INTERVAL == 0) {
            long fpsEnd = SystemClock.elapsedRealtime();
            double fps = (double)(fpsEnd - fpsStart) / (double)fpsFrames;

            Log.d(TAG, String.format("FPS: %.2f", fps));

            fpsStart = fpsEnd;
            fpsFrames = 0;
        }
    }
}
