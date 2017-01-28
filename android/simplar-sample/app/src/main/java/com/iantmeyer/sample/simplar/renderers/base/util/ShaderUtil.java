package com.iantmeyer.sample.simplar.renderers.base.util;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderUtil {

    public static String getRawShader(Context context, @RawRes int shaderId) {
        return readRawTextFile(context, shaderId);
    }

    public static void attachShaders(Context context, int program, int vertexShaderId, int fragmentShaderId) {
        String vertexShader = ShaderUtil.getRawShader(context, vertexShaderId);
        String fragmentShader = ShaderUtil.getRawShader(context, fragmentShaderId);
        attachShaders(program, vertexShader, fragmentShader);
    }

    public static void attachShaders(int program, String vertexShader, String fragmentShader) {
        int vShader = loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShader
        );
        int fShader = loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShader
        );
        GLES20.glAttachShader(program, vShader);    // add the vertex shader to program
        GLES20.glAttachShader(program, fShader);    // add the fragment shader to program
    }

    // TODO IM - use checkGlError()

    /**
     * Utility method for compiling a OpenGL shader.
     * <p>
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type       - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    private static int loadShader(int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    // TODO IM

    /**
     * Converts a raw text file into a string.
     *
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The context of the text file, or null in case of error.
     */
    private static String readRawTextFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}