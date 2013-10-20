package com.bazbatlabs.bricksmash.models;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.util.Log;
import android.content.res.Resources;

import static android.opengl.GLES20.*;

public final class ShaderCompiler {

    public static int buildProgram(int vertexResourceId, int fragmentResourceId,
                                   Resources resources) {

        String vertexSource = readResource(resources, vertexResourceId);
        String fragmentSource = readResource(resources, fragmentResourceId);

        // TODO: check for error return values
        int vertexShader = compileShader(GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            Log.w(TAG, "Compilation failure for vertex shader.");
            return 0;
        }

        int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            Log.w(TAG, "Compilation failure for fragment shader.");
            glDeleteShader(vertexShader);
            return 0;
        }

        int program = linkProgram(vertexShader, fragmentShader);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        if (program == 0) {
            Log.w(TAG, "Linking failure.");
            return 0;
        }

        if (!validateProgram(program)) {
            Log.w(TAG, "Program failed validation.");
            glDeleteProgram(program);
            return 0;
        }

        return program;
    }

    private static String readResource(Resources resources, int id) {
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

    private static int compileShader(int type, String code) {

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

    private static int linkProgram(int vertexShaderId, int fragmentShaderId) {

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

    private static boolean validateProgram(int programId) {
        glValidateProgram(programId);

        final int[] status = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, status, 0);

        Log.v(TAG, "Validation status: " + status[0] +
              "\nLog: " + glGetProgramInfoLog(programId));

        return status[0] != 0;
    }

    private static final String TAG = "ShaderCompiler";
}
