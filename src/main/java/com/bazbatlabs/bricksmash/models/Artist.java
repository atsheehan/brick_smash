package com.bazbatlabs.bricksmash.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.FloatBuffer;

import android.util.Log;
import android.content.res.Resources;
import android.opengl.Matrix;

import static android.opengl.GLES20.*;

import com.bazbatlabs.bricksmash.R;

public final class Artist {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;
    private final float[] vertices;

    private int vertexIndex;
    private int spriteCount;

    private final float[] projectionMatrix;

    private int aColorLoc;
    private int aPositionLoc;
    private int aTextureCoordinatesLoc;
    private int uMatrixLoc;
    private int uTextureUnitLoc;

    private int program;
    private int texture;

    public Artist(Resources resources, int screenWidth, int screenHeight) {

        this.texture = 0;

        this.program = ShaderCompiler.buildProgram(R.raw.vertex_shader,
                                                   R.raw.fragment_shader,
                                                   resources);
        if (this.program == 0) {
            throw new RuntimeException("Failed to build OpenGL program. Check log for details.");
        }

        glUseProgram(this.program);

        this.aColorLoc = glGetAttribLocation(this.program, A_COLOR);
        this.aPositionLoc = glGetAttribLocation(this.program, A_POSITION);
        this.aTextureCoordinatesLoc = glGetAttribLocation(this.program, A_TEXTURE_COORDINATES);

        this.uMatrixLoc = glGetUniformLocation(this.program, U_MATRIX);
        this.uTextureUnitLoc = glGetUniformLocation(this.program, U_TEXTURE_UNIT);

        this.projectionMatrix = new float[16];
        changeSurface(screenWidth, screenHeight);

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

        vertexBuffer.position(POSITION_START);
        glVertexAttribPointer(aPositionLoc, FLOATS_PER_POSITION, GL_FLOAT,
                              false, BYTES_PER_VERTEX, vertexBuffer);

        glEnableVertexAttribArray(aPositionLoc);

        vertexBuffer.position(TEXTURE_START);
        glVertexAttribPointer(aTextureCoordinatesLoc, FLOATS_PER_TEXTURE, GL_FLOAT,
                              false, BYTES_PER_VERTEX, vertexBuffer);

        glEnableVertexAttribArray(aTextureCoordinatesLoc);

        vertexBuffer.position(COLOR_START);
        glVertexAttribPointer(aColorLoc, FLOATS_PER_COLOR, GL_FLOAT,
                              false, BYTES_PER_VERTEX, vertexBuffer);

        glEnableVertexAttribArray(aColorLoc);
        glViewport(0, 0, screenWidth, screenHeight);
    }

    public void changeSurface(int width, int height) {
        Vec2 dimensions = gameDimensions(width, height);

        float xOffset = (dimensions.x() - World.WIDTH) / 2f;
        float yOffset = (dimensions.y() - World.HEIGHT) / 2f;

        float left = -xOffset;
        float right = World.WIDTH + xOffset;
        float bottom = -yOffset;
        float top = World.HEIGHT + yOffset;

        Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, -1f, 1f);
        glViewport(0, 0, width, height);
    }

    public void startDrawing() {
        vertexIndex = 0;
        spriteCount = 0;
    }

    public void finishDrawing() {
        vertexBuffer.clear();
        vertexBuffer.put(vertices);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLoc, 1, false, projectionMatrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        glUniform1i(uTextureUnitLoc, 0);

        glDrawElements(GL_TRIANGLES, INDICES_PER_SPRITE * spriteCount,
                       GL_UNSIGNED_SHORT, indexBuffer);
    }

    public void drawRect(Rect bounds, Color color) {
        drawRect(bounds.origin(), bounds.size(), color);
    }

    public void drawRect(Vec2 origin, Vec2 size, Color color) {
        addVertex(origin.x(), origin.y(), color);
        addVertex(origin.x() + size.x(), origin.y(), color);
        addVertex(origin.x() + size.x(), origin.y() + size.y(), color);
        addVertex(origin.x(), origin.y() + size.y(), color);

        spriteCount++;
    }

    public void drawImage(Rect bounds, Image image) {
        drawImage(bounds.origin(), bounds.size(), image, Color.WHITE());
    }

    public void drawImage(Rect bounds, Image image, Color color) {
        drawImage(bounds.origin(), bounds.size(), image, color);
    }

    public void drawImage(Vec2 origin, Vec2 size, Image image) {
        drawImage(origin, size, image, Color.WHITE());
    }

    public void drawImage(Vec2 origin, Vec2 size, Image image, Color color) {
        addVertex(origin.x(), origin.y(), image.x(), image.y() + image.h(), color);
        addVertex(origin.x() + size.x(), origin.y(), image.x() + image.w(), image.y() + image.h(), color);
        addVertex(origin.x() + size.x(), origin.y() + size.y(), image.x() + image.w(), image.y(), color);
        addVertex(origin.x(), origin.y() + size.y(), image.x(), image.y(), color);

        texture = image.textureId();

        spriteCount++;
    }

    private void addVertex(float x, float y) {
        addVertex(x, y, Color.WHITE());
    }

    private void addVertex(float x, float y, Color color) {
        addVertex(x, y, 0f, 0f, color);
    }

    private void addVertex(float x, float y, float texX, float texY) {
        addVertex(x, y, texX, texY, Color.WHITE());
    }

    private void addVertex(float x, float y, float texX, float texY, Color color) {
        vertices[vertexIndex++] = x;
        vertices[vertexIndex++] = y;
        vertices[vertexIndex++] = texX;
        vertices[vertexIndex++] = texY;
        vertices[vertexIndex++] = color.r();
        vertices[vertexIndex++] = color.g();
        vertices[vertexIndex++] = color.b();
        vertices[vertexIndex++] = color.a();
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

    private static final float GAME_HEIGHT = World.HEIGHT * 1.2f;
    private static final float GAME_WIDTH = World.WIDTH * 1.2f;

    private static final int FLOATS_PER_POSITION = 2;
    private static final int FLOATS_PER_TEXTURE = 2;
    private static final int FLOATS_PER_COLOR = 4;

    private static final int POSITION_START = 0;
    private static final int TEXTURE_START = POSITION_START + FLOATS_PER_POSITION;
    private static final int COLOR_START = TEXTURE_START + FLOATS_PER_TEXTURE;

    private static final int FLOATS_PER_VERTEX =
        FLOATS_PER_POSITION + FLOATS_PER_TEXTURE + FLOATS_PER_COLOR;

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;

    private static final int BYTES_PER_VERTEX =
        FLOATS_PER_VERTEX * BYTES_PER_FLOAT;

    private static final int VERTICES_PER_SPRITE = 4;
    private static final int INDICES_PER_SPRITE = 6;

    private static final int MAX_SPRITES = 1000;
    private static final int MAX_VERTICES = MAX_SPRITES * VERTICES_PER_SPRITE;
    private static final int MAX_INDICES = MAX_SPRITES * INDICES_PER_SPRITE;

    private static final int VERTEX_BUFFER_SIZE =
        MAX_VERTICES * BYTES_PER_VERTEX;

    private static final int VERTEX_ARRAY_SIZE =
        MAX_VERTICES * FLOATS_PER_VERTEX;

    private static final int INDEX_BUFFER_SIZE =
        MAX_INDICES * BYTES_PER_SHORT;

    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";

    private static final String TAG = "Artist";
}
