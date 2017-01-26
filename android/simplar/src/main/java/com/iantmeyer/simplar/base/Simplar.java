package com.iantmeyer.simplar.base;

import android.graphics.PixelFormat;
import android.hardware.camera2.CameraAccessException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceView;

import com.google.vr.ndk.base.GvrSurfaceView;
import com.google.vr.sdk.base.GvrView2;
import com.iantmeyer.simplar.camera.CameraController;
import com.iantmeyer.simplar.camera.CameraPermissionCallback;
import com.iantmeyer.simplar.render.ArRenderManager;
import com.iantmeyer.simplar.render.ArRenderer;

import java.util.List;
import java.util.Objects;

public class Simplar implements ArApi {

    private static final String TAG = "Simplar";

    private ArRenderManager mRendererManager;

    private CameraController mCameraController;

    public Simplar() {
        mRendererManager = new ArRenderManager();
    }

    @Override
    public void setGvrView(@NonNull GvrView2 gvrView) {
        GvrSurfaceView surfaceView = gvrView.getGvrSurfaceView();
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setZOrderMediaOverlay(true);

        gvrView.setStereoModeEnabled(false);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        gvrView.setRenderer(mRendererManager);
    }

    @Override
    public void setCameraSurfaceView(@NonNull SurfaceView cameraSurfaceView) {
        try {
            mCameraController = new CameraController(cameraSurfaceView, this);
        } catch (CameraAccessException e) {
            Log.e(TAG, "CameraAccessException: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void setFov(@NonNull float[] fieldOfView) {
        mRendererManager.setFov(fieldOfView);
    }

    @Override
    public void startCamera(CameraPermissionCallback cameraCallback) {
        try {
            mCameraController.startCamera(cameraCallback);
        } catch (CameraAccessException e) {
            Log.e(TAG, "CameraAccessException: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void stopCamera() {
        mCameraController.stopCamera();
    }

    @Override
    public void setManualNorth() {
        mRendererManager.setManualNorth();
    }

    @Override
    public int addRenderer(@NonNull ArRenderer renderer) {
        return mRendererManager.addRenderer(renderer);
    }

    @Override
    public void addRendererList(@NonNull List<ArRenderer> rendererList) {
        Objects.requireNonNull(rendererList);
        mRendererManager.addRenderers(rendererList);
    }
}