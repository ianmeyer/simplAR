package com.iantmeyer.sample.simplar.renderers.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import com.iantmeyer.sample.simplar.R;
import com.iantmeyer.sample.simplar.renderers.base.util.RenderUtil;
import com.iantmeyer.sample.simplar.renderers.base.util.ShaderUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class MultiColorMesh extends BaseRenderer {

    private Context mContext;

    private final float[] mVertexCoordinates;
    private final short[] mDrawOrder;
    private float[] mColors;
    private final float[] mPosition;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int BYTES_PER_COORD = 4;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_COORD;

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private FloatBuffer mColorsBuffer;

    private int mPositionHandle;
    private int mColorsHandle;
    private int mMvpMatrixHandle;

    public MultiColorMesh(Context context,
                          CoordinateSystem coordinateSystem,
                          @NonNull float[] vertexCoordinates,
                          @NonNull short[] drawOrder,
                          @NonNull float[] colors,
                          @NonNull float[] position
    ) {
        super(coordinateSystem);
        mContext = context;
        mVertexCoordinates = vertexCoordinates;
        mDrawOrder = drawOrder;
        mColors = colors;
        mPosition = position;
    }

    @Override
    protected void buildBuffers() {
        mVertexBuffer = RenderUtil.buildFloatBuffer(mVertexCoordinates);
        mDrawListBuffer = RenderUtil.buildShortBuffer(mDrawOrder);
        mColorsBuffer = RenderUtil.buildFloatBuffer(mColors);
    }

    @Override
    protected String getVertexShader() {
        return ShaderUtil.getRawShader(mContext, R.raw.vertex_multi_color);
    }

    @Override
    protected String getFragmentShader() {
        return ShaderUtil.getRawShader(mContext, R.raw.fragment_multi_color);
    }

    @Override
    protected void getHandles(int programHandle) {
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "aPosition");
        mColorsHandle = GLES20.glGetAttribLocation(programHandle, "aColors");
        mMvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix");
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
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                mVertexBuffer
        );
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(
                mColorsHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                0,
                mColorsBuffer
        );
        GLES20.glEnableVertexAttribArray(mColorsHandle);

        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                mDrawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,
                mDrawListBuffer
        );

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorsHandle);
    }
}