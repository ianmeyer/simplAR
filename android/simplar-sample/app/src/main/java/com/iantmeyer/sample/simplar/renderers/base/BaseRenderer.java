package com.iantmeyer.sample.simplar.renderers.base;

import android.opengl.GLES20;
import android.support.annotation.NonNull;

import com.iantmeyer.sample.simplar.renderers.base.util.ShaderUtil;
import com.iantmeyer.simplar.render.ArRenderer;
import com.iantmeyer.simplar.render.CoordinateSystem;

import javax.microedition.khronos.egl.EGLConfig;

abstract class BaseRenderer implements ArRenderer {

    private int mProgramHandle;

    private final CoordinateSystem mCoordinateSystem;

    BaseRenderer(CoordinateSystem coordinateSystem) {
        mCoordinateSystem = coordinateSystem;
    }

    protected abstract void buildBuffers();

    protected abstract String getVertexShader();

    protected abstract String getFragmentShader();

    protected abstract void getHandles(int programHandle);

    protected abstract void applyTransformations(float[] mvpMatrix);

    protected abstract void drawModels();

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        this.buildBuffers();

        mProgramHandle = GLES20.glCreateProgram();

        ShaderUtil.attachShaders(mProgramHandle, getVertexShader(), getFragmentShader());

        GLES20.glLinkProgram(mProgramHandle);

        this.getHandles(mProgramHandle);
    }

    @Override
    public void onDraw(@NonNull float[] mvpMatrix) {
        GLES20.glUseProgram(mProgramHandle);

        this.applyTransformations(mvpMatrix);

        this.drawModels();
    }

    @Override
    public CoordinateSystem getCoordinateSystem() {
        return mCoordinateSystem;
    }
}
