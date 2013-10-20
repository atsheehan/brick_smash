package com.bazbatlabs.bricksmash.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import static android.opengl.GLES20.*;

public final class TextureLoader {

    public static Texture load(int resourceId, Resources resources) {

        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);

        if (textureIds[0] == 0) {
            Log.w(TAG, "Could not create new OpenGL texture object.");
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory
            .decodeResource(resources, resourceId, options);

        if (bitmap == null) {
            Log.w(TAG, String.format("Resource ID %d could not be decoded.", resourceId));

            glDeleteTextures(1, textureIds, 0);
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        glBindTexture(GL_TEXTURE_2D, textureIds[0]);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        glBindTexture(GL_TEXTURE_2D, 0);

        return new Texture(textureIds[0], width, height);
    }

    private static final String TAG = "TextureLoader";
}
