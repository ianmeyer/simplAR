package com.iantmeyer.sample.simplar.renderers.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import com.iantmeyer.sample.simplar.R;
import com.iantmeyer.sample.simplar.renderers.base.util.RenderUtil;
import com.iantmeyer.sample.simplar.renderers.base.util.ShaderUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextureMesh extends BaseRenderer {

    private Context mContext;

    private final float[] mVertexCoordinates;
    private final short[] mDrawOrder;
    private float[] mPosition;
    private float[] mTextureCoordinates;
    private Bitmap mTextureBitmap;

    // TODO IM - Support for multiple Textures?
    private int[] mTextures;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int BYTES_PER_COORD = 4;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_COORD;

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private FloatBuffer mTextureBuffer;

    private int mPositionHandle;
    private int mMvpMatrixHandle;

    private int mTextureHandle;
    private int mTextureCoordinateHandle;

    public TextureMesh(Context context,
                       CoordinateSystem coordinateSystem,
                       @NonNull float[] vertexCoordinates,
                       @NonNull short[] drawOrder,
                       @NonNull float[] position,
                       @NonNull float[] textureCoordinates,
                       @NonNull Bitmap bitmap) {
        super(coordinateSystem);
        mContext = context;
        mVertexCoordinates = vertexCoordinates;
        mDrawOrder = drawOrder;
        mPosition = position;
        mTextureCoordinates = textureCoordinates;
        mTextureBitmap = bitmap;
    }

    @Override
    protected void buildBuffers() {
        mVertexBuffer = RenderUtil.buildFloatBuffer(mVertexCoordinates);
        mDrawListBuffer = RenderUtil.buildShortBuffer(mDrawOrder);
        mTextureBuffer = RenderUtil.buildFloatBuffer(mTextureCoordinates);
    }

    @Override
    protected String getVertexShader() {
        return ShaderUtil.getRawShader(mContext, R.raw.vertex_texture);
    }

    @Override
    protected String getFragmentShader() {
        return ShaderUtil.getRawShader(mContext, R.raw.fragment_texture);
    }

    @Override
    protected void getHandles(int programHandle) {
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "aPosition");
        mMvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix");

        // TODO IM: Create separate function for textures?
        mTextures = new int[1];
        GLES20.glGenTextures(1, mTextures, 0);
        // Bind texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        // Set wrapping
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mTextureBitmap, 0);
        // TODO IM: bitmap cleanup?
//            if (mTextureBitmap != null) {
//                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mTextureBitmap, 0);
//                mTextureBitmap.recycle();
//            }
        mTextureHandle = GLES20.glGetUniformLocation(programHandle, "sTexture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "aTextureCoords");
    }

    @Override
    protected void applyTransformations(float[] mvpMatrix) {
        if (mPosition != null) {
            Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mPosition, 0);
        }
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, mvpMatrix, 0);
    }

    @Override
    protected void drawModels() {
        // Vertex coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                mVertexBuffer
        );
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Activate and bind texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLES20.glVertexAttribPointer(
                mTextureCoordinateHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                mTextureBuffer
        );
        GLES20.glUniform1i(mTextureHandle, 0);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        // Draw the mesh
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                mDrawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,
                mDrawListBuffer
        );

        // Disable vertex arrays
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
    }
}