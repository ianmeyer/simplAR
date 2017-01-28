package com.iantmeyer.sample.simplar.renderers.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.iantmeyer.sample.simplar.R;
import com.iantmeyer.sample.simplar.renderers.base.util.RenderUtil;
import com.iantmeyer.sample.simplar.renderers.base.util.ShaderUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class LineRenderer extends BaseRenderer {

    private Context mContext;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int BYTES_PER_COORD = 4;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_COORD;

    private final float[] mCoords;
    private final short[] mDrawOrder;
    private final float[] mColor;
    private float mLineWidth = 1.0f;
    private float[] mPosition;

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    public LineRenderer(Context context, CoordinateSystem coordinateSystem, float[] coords, short[] drawOrder,
                        float[] color, float lineWidth, float[] position) {
        super(coordinateSystem);
        mContext = context;
        mCoords = coords;
        mDrawOrder = drawOrder;
        mColor = color;
        mLineWidth = lineWidth;
        mPosition = position;
    }

    @Override
    protected void buildBuffers() {
        mVertexBuffer = RenderUtil.buildFloatBuffer(mCoords);
        mDrawListBuffer = RenderUtil.buildShortBuffer(mDrawOrder);
    }

    @Override
    protected String getVertexShader() {
        return ShaderUtil.getRawShader(mContext, R.raw.vertex_single_color);
    }

    @Override
    protected String getFragmentShader() {
        return ShaderUtil.getRawShader(mContext, R.raw.fragment_single_color);
    }

    @Override
    protected void getHandles(int programHandle) {
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "aPosition");
        mColorHandle = GLES20.glGetUniformLocation(programHandle, "vColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix");
    }

    @Override
    protected void applyTransformations(float[] mvpMatrix) {
        if (mPosition != null) {
            Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mPosition, 0);
        }
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
    }

    @Override
    protected void drawModels() {
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                mVertexBuffer
        );

        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);

        GLES20.glLineWidth(mLineWidth);

        GLES20.glDrawElements(
                GLES20.GL_LINES,
                mDrawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,
                mDrawListBuffer
        );

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisable(mColorHandle);
    }
}