package com.iantmeyer.simplar.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import java.util.List;
import java.util.Objects;

import javax.microedition.khronos.egl.EGLConfig;

public class ArRenderManager implements GvrView.StereoRenderer {

    private static final String TAG = "ArRenderManager";

    // TODO IM: What are good near/far values? Should this be settable?
    private static final float FUSTRUM_NEAR = 0.1f;
    private static final float FUSTRUM_FAR = 100.0f;

    // TODO IM: What is a good camera Z-offset value? Should this be settable?
    private static final float CAMERA_Z = 0.01f;

    private int mRendererKey = 0;
    private final SparseArray<ArRenderer> mRenderers = new SparseArray<>();

    private final float[] mCameraMatrix;
    private final float[] mEulerAngles;
    private final float[] mModelViewMatrix;
    private float[] mPerspectiveMatrix;
    private final float[] mMvpMatrix;

    private boolean mSetNorth = false;

    public ArRenderManager() {
        mCameraMatrix = new float[16];
        mModelViewMatrix = new float[16];
        mPerspectiveMatrix = new float[16];
        mMvpMatrix = new float[16];
        mEulerAngles = new float[16];
    }

    /**
     * Add a renderer and return it's reference key
     *
     * @param renderer
     * @return renderer key, as Integer
     */
    public int addRenderer(@NonNull ArRenderer renderer) {
        Objects.requireNonNull(renderer);

        mRenderers.put(mRendererKey, renderer);
        mRendererKey++;
        return mRendererKey - 1;
    }

    public void addRenderers(@NonNull List<ArRenderer> rendererList) {
        Objects.requireNonNull(rendererList);
        for (ArRenderer renderer : rendererList) {
            this.addRenderer(renderer);
        }
    }

    public void removeRenderer(int key) {
        mRenderers.remove(key);
    }

    public void clearRenderers() {
        mRenderers.clear();
        mRendererKey = 0;
    }

    /**
     * @param fieldOfView float array <br>
     *                    element 0: horizontal field of view <br>
     *                    element 1: camera aspect ratio
     */
    public void setFov(@NonNull float[] fieldOfView) {
        Matrix.perspectiveM(mPerspectiveMatrix, 0, (float) (fieldOfView[0] * 180 / Math.PI), fieldOfView[1], FUSTRUM_NEAR, FUSTRUM_FAR);
    }

    /**
     * Will set True North to be the look direction of the next frame
     * (projected on ground plane)
     */
    public void setManualNorth() {
        mSetNorth = true;
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated()");
        // Set transparent background for camera pass through
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        // Enable depth test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        for (int idx = 0; idx < mRenderers.size(); idx++) {
            ArRenderer arRenderer = mRenderers.valueAt(idx);
            arRenderer.onSurfaceCreated(eglConfig);
        }
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mCameraMatrix, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        // Set the north direction manually in the direction the device is currently facing
        if (mSetNorth) {
            mSetNorth = false;
            headTransform.getEulerAngles(mEulerAngles, 0);
            for (int idx = 0; idx < mRenderers.size(); idx++) {
                ArRenderer arRenderer = mRenderers.valueAt(idx);
                arRenderer.getCoordinateSystem().setNorthOffset(mEulerAngles[1]);
            }
        }
    }

    @Override
    public void onDrawEye(Eye eye) {
        if (mPerspectiveMatrix == null) {
            // TODO IM: Split view?
            mPerspectiveMatrix = eye.getPerspective(FUSTRUM_NEAR, FUSTRUM_FAR);
        }
        for (int idx = 0; idx < mRenderers.size(); idx++) {
            ArRenderer arRenderer = mRenderers.valueAt(idx);

            arRenderer.getCoordinateSystem().getModelViewMatrix(mModelViewMatrix, eye.getEyeView(), mCameraMatrix);

            Matrix.multiplyMM(mMvpMatrix, 0, mPerspectiveMatrix, 0, mModelViewMatrix, 0);
            arRenderer.onDraw(mMvpMatrix);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    @Override
    public void onRendererShutdown() {
    }
}