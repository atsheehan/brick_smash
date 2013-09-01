package com.bazbatlabs.smashballs.models;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.FloatBuffer;

import android.util.Log;
import android.content.res.Resources;
import android.opengl.Matrix;

import static android.opengl.GLES20.*;

import com.bazbatlabs.smashballs.R;

public final class Artist {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;
    private final float[] vertices;

    private int vertexIndex;
    private int spriteCount;

    private final float[] projectionMatrix;

    private int aColorLoc;
    private int aPositionLoc;
    private int uMatrixLoc;

    private int program;

    public Artist(Resources resources, int screenWidth, int screenHeight) {
        String vertexSource = readResource(resources, R.raw.vertex_shader);
        String fragmentSource = readResource(resources, R.raw.fragment_shader);

        // TODO: check for error return values
        int vertexShader = compileShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentSource);

        this.program = linkProgram(vertexShader, fragmentShader);

        validateProgram(this.program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        glUseProgram(this.program);

        this.aColorLoc = glGetAttribLocation(this.program, A_COLOR);
        this.aPositionLoc = glGetAttribLocation(this.program, A_POSITION);
        this.uMatrixLoc = glGetUniformLocation(this.program, U_MATRIX);

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

        vertexBuffer.position(0);
        glVertexAttribPointer(aPositionLoc, FLOATS_PER_POSITION, GL_FLOAT,
                              false, BYTES_PER_VERTEX, vertexBuffer);

        glEnableVertexAttribArray(aPositionLoc);

        vertexBuffer.position(FLOATS_PER_POSITION);
        glVertexAttribPointer(aColorLoc, FLOATS_PER_COLOR, GL_FLOAT,
                              false, BYTES_PER_VERTEX, vertexBuffer);

        glEnableVertexAttribArray(aColorLoc);
    }

    public void changeSurface(int width, int height) {
        Vec2 dimensions = gameDimensions(width, height);

        Matrix.orthoM(projectionMatrix, 0, 0f, dimensions.x, 0f, dimensions.y, -1f, 1f);
        glViewport(0, 0, width, height);
    }

    public void startDrawing() {
        vertexIndex = 0;
        spriteCount = 0;
    }

    public void finishDrawing(int screenWidth, int screenHeight) {
        vertexBuffer.clear();
        vertexBuffer.put(vertices);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glViewport(0, 0, screenWidth, screenHeight);

        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLoc, 1, false, projectionMatrix, 0);

        glDrawElements(GL_TRIANGLES, INDICES_PER_SPRITE * spriteCount,
                       GL_UNSIGNED_SHORT, indexBuffer);
    }

    public void drawRect(Rect bounds, Color color) {
        drawRect(bounds.origin, bounds.size, color);
    }

    public void drawRect(Vec2 origin, Vec2 size, Color color) {
        addVertex(origin.x, origin.y, color);
        addVertex(origin.x + size.x, origin.y, color);
        addVertex(origin.x + size.x, origin.y + size.y, color);
        addVertex(origin.x, origin.y + size.y, color);

        spriteCount++;
    }

    private void addVertex(float x, float y) {
        addVertex(x, y, Color.WHITE);
    }

    private void addVertex(float x, float y, Color color) {
        vertices[vertexIndex++] = x;
        vertices[vertexIndex++] = y;
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

    private String readResource(Resources resources, int id) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream in = resources.openRawResource(id);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line);
                body.append('\n');
            }

        } catch (IOException ioe) {
            throw new RuntimeException("Could not open resource: " + id, ioe);

        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not found: " + id, nfe);
        }

        return body.toString();
    }

    private int compileShader(int type, String code) {

        final int shaderId = glCreateShader(type);

        if (shaderId == 0) {
            Log.w(TAG, "Could not create new shader.");
            return 0;
        }

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);

        final int[] status = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0);

        Log.v(TAG, "Shader compilation status: " + glGetShaderInfoLog(shaderId));

        if (status[0] == 0) {
            glDeleteShader(shaderId);
            Log.w(TAG, "Shader compilation failed.");

            return 0;
        }

        return shaderId;
    }

    private int linkProgram(int vertexShaderId, int fragmentShaderId) {

        final int programId = glCreateProgram();

        if (programId == 0) {
            Log.w(TAG, "Could not create new program.");
            return 0;
        }

        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        glLinkProgram(programId);

        final int[] status = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, status, 0);

        Log.v(TAG, "Program link status: " + glGetProgramInfoLog(programId));

        if (status[0] == 0) {
            glDeleteProgram(programId);
            Log.w(TAG, "Program linking failed.");

            return 0;
        }

        return programId;
    }

    private boolean validateProgram(int programId) {
        glValidateProgram(programId);

        final int[] status = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, status, 0);

        Log.v(TAG, "Validation status: " + status[0] +
              "\nLog: " + glGetProgramInfoLog(programId));

        return status[0] != 0;
    }

    private static final float GAME_HEIGHT = 360.0f;

    private static final int FLOATS_PER_POSITION = 2;
    private static final int FLOATS_PER_TEXTURE = 2;
    private static final int FLOATS_PER_COLOR = 4;

    private static final int FLOATS_PER_VERTEX =
        FLOATS_PER_POSITION + FLOATS_PER_COLOR;

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
    private static final String U_MATRIX = "u_Matrix";

    private static final String TAG = "Artist";
}
