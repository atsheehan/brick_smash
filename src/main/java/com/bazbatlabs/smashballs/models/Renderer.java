package com.bazbatlabs.smashballs.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

public final class Renderer {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;
    private final float[] vertices;

    private int vertexIndex;
    private int spriteCount;

    public Renderer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(VERTEX_BUFFER_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        this.vertexBuffer = buffer.asFloatBuffer();
        this.vertices = new float[VERTEX_ARRAY_SIZE];

        buffer = ByteBuffer.allocateDirect(INDEX_BUFFER_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        this.indexBuffer = buffer.asShortBuffer();

        short[] indices = new short[MAX_INDICES];

        for (int i = 0, j = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short)(j + 0);
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = (short)(j + 0);
        }

        this.indexBuffer.put(indices, 0, indices.length);
        this.indexBuffer.flip();

        this.vertexIndex = 0;
        this.spriteCount = 0;
    }

    public void startDrawing(GL10 gl, int textureId) {
        vertexIndex = 0;
        spriteCount = 0;

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void finishDrawing(GL10 gl, int screenWidth, int screenHeight) {
        Vec2 dimensions = gameDimensions(screenWidth, screenHeight);

        vertexBuffer.clear();
        vertexBuffer.put(vertices, 0, vertexIndex);
        vertexBuffer.flip();

        gl.glViewport(0, 0, screenWidth, screenHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, dimensions.x, 0, dimensions.y, 1, -1);

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        if (spriteCount > 0) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            vertexBuffer.position(0);
            gl.glVertexPointer(FLOATS_PER_COORDINATE, GL10.GL_FLOAT,
                               BYTES_PER_VERTEX, vertexBuffer);

            vertexBuffer.position(FLOATS_PER_COORDINATE);
            gl.glTexCoordPointer(FLOATS_PER_TEXTURE, GL10.GL_FLOAT,
                                 BYTES_PER_VERTEX, vertexBuffer);

            vertexBuffer.position(FLOATS_PER_COORDINATE + FLOATS_PER_TEXTURE);
            gl.glColorPointer(FLOATS_PER_COLOR, GL10.GL_FLOAT,
                              BYTES_PER_VERTEX, vertexBuffer);

            gl.glDrawElements(GL10.GL_TRIANGLES, spriteCount * INDICES_PER_SPRITE,
                              GL10.GL_UNSIGNED_SHORT, indexBuffer);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDisable(GL10.GL_BLEND);
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }

        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    public void drawRect(Vec2 origin, Vec2 size, Color color) {
        addVertex(origin.x, origin.y, color);
        addVertex(origin.x + size.x, origin.y, color);
        addVertex(origin.x + size.x, origin.y + size.y, color);
        addVertex(origin.x, origin.y + size.y, color);

        spriteCount++;
    }

    public void drawRect(Vec2 origin, Vec2 size, float angle, Color color) {

        float halfWidth = size.x / 2;
        float halfHeight = size.y / 2;

        float cos = FloatMath.cos(angle);
        float sin = FloatMath.sin(angle);

        float xCenter = origin.x + halfWidth;
        float yCenter = origin.y + halfHeight;

        float x1 = (-halfWidth * cos - (-halfHeight) * sin) + xCenter;
        float y1 = (-halfWidth * sin + (-halfHeight) * cos) + yCenter;
        float x2 = (halfWidth * cos - (-halfHeight) * sin) + xCenter;
        float y2 = (halfWidth * sin + (-halfHeight) * cos) + yCenter;
        float x3 = (halfWidth * cos - halfHeight * sin) + xCenter;
        float y3 = (halfWidth * sin + halfHeight * cos) + yCenter;
        float x4 = (-halfWidth * cos - halfHeight * sin) + xCenter;
        float y4 = (-halfWidth * sin + halfHeight * cos) + yCenter;

        addVertex(x1, y1, color);
        addVertex(x2, y2, color);
        addVertex(x3, y3, color);
        addVertex(x4, y4, color);

        spriteCount++;
    }

    public void drawImage(Vec2 origin, Vec2 size, Image image) {
        addVertex(origin.x, origin.y, image.x, image.y + image.h);
        addVertex(origin.x + size.x, origin.y, image.x + image.w, image.y + image.h);
        addVertex(origin.x + size.x, origin.y + size.y, image.x + image.w, image.y);
        addVertex(origin.x, origin.y + size.y, image.x, image.y);

        spriteCount++;
    }

    public void drawImage(Vec2 origin, Vec2 size, float angle, Image image) {

        float halfWidth = size.x / 2;
        float halfHeight = size.y / 2;

        float cos = FloatMath.cos(angle);
        float sin = FloatMath.sin(angle);

        float xCenter = origin.x + halfWidth;
        float yCenter = origin.y + halfHeight;

        float x1 = (-halfWidth * cos - (-halfHeight) * sin) + xCenter;
        float y1 = (-halfWidth * sin + (-halfHeight) * cos) + yCenter;
        float x2 = (halfWidth * cos - (-halfHeight) * sin) + xCenter;
        float y2 = (halfWidth * sin + (-halfHeight) * cos) + yCenter;
        float x3 = (halfWidth * cos - halfHeight * sin) + xCenter;
        float y3 = (halfWidth * sin + halfHeight * cos) + yCenter;
        float x4 = (-halfWidth * cos - halfHeight * sin) + xCenter;
        float y4 = (-halfWidth * sin + halfHeight * cos) + yCenter;

        addVertex(x1, y1, image.x, image.y + image.h);
        addVertex(x2, y2, image.x + image.w, image.y + image.h);
        addVertex(x3, y3, image.x + image.w, image.y);
        addVertex(x4, y4, image.x, image.y);

        spriteCount++;
    }

    private void addVertex(float x, float y) {
        addVertex(x, y, 0.0f, 0.0f, Color.WHITE);
    }

    private void addVertex(float x, float y, Color color) {
        addVertex(x, y, 0.0f, 0.0f, color);
    }

    private void addVertex(float x, float y, float texX, float texY) {
        addVertex(x, y, texX, texY, Color.WHITE);
    }

    private void addVertex(float x, float y, float texX, float texY, Color color) {
        vertices[vertexIndex++] = x;
        vertices[vertexIndex++] = y;
        vertices[vertexIndex++] = texX;
        vertices[vertexIndex++] = texY;
        vertices[vertexIndex++] = color.r;
        vertices[vertexIndex++] = color.g;
        vertices[vertexIndex++] = color.b;
        vertices[vertexIndex++] = color.a;
    }

    /**
     * Determine how much of the game world should be shown based on the screen
     * dimensions. Used to prevent the game objects from being stretched on
     * different screen width/height ratios. In this case, the height is constant
     * but the width will vary.
     */
    private Vec2 gameDimensions(float screenWidth, float screenHeight) {
        if (screenHeight == 0.0f) {
            return new Vec2(0.0f, 0.0f);
        } else {
            float ratio = screenWidth / screenHeight;
            return new Vec2(GAME_HEIGHT * ratio, GAME_HEIGHT);
        }
    }

    private final static float GAME_HEIGHT = 360.0f;

    private final static int FLOATS_PER_COORDINATE = 2;
    private final static int FLOATS_PER_TEXTURE = 2;
    private final static int FLOATS_PER_COLOR = 4;

    private final static int FLOATS_PER_VERTEX =
        FLOATS_PER_COORDINATE + FLOATS_PER_TEXTURE + FLOATS_PER_COLOR;

    private final static int BYTES_PER_FLOAT = 4;
    private final static int BYTES_PER_SHORT = 2;

    private final static int BYTES_PER_VERTEX =
        FLOATS_PER_VERTEX * BYTES_PER_FLOAT;

    private final static int VERTICES_PER_SPRITE = 4;
    private final static int INDICES_PER_SPRITE = 6;

    private final static int MAX_SPRITES = 1000;
    private final static int MAX_VERTICES = MAX_SPRITES * VERTICES_PER_SPRITE;
    private final static int MAX_INDICES = MAX_SPRITES * INDICES_PER_SPRITE;

    private final static int VERTEX_BUFFER_SIZE =
        MAX_VERTICES * BYTES_PER_VERTEX;

    private final static int VERTEX_ARRAY_SIZE =
        MAX_VERTICES * FLOATS_PER_VERTEX;

    private final static int INDEX_BUFFER_SIZE =
        MAX_INDICES * BYTES_PER_SHORT;
}
