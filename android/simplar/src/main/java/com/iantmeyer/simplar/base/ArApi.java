package com.iantmeyer.simplar.base;

import android.support.annotation.NonNull;
import android.view.SurfaceView;

import com.google.vr.sdk.base.GvrView2;
import com.iantmeyer.simplar.camera.CameraPermissionCallback;
import com.iantmeyer.simplar.render.ArRenderer;

import java.util.List;

public interface ArApi {
    void setGvrView(@NonNull GvrView2 gvrView);

    void setCameraSurfaceView(@NonNull SurfaceView cameraSurfaceView);

    void setFov(@NonNull float[] fov);

    void startCamera(CameraPermissionCallback cameraCallback);

    void stopCamera();

    int addRenderer(@NonNull ArRenderer renderer);

    void addRendererList(@NonNull List<ArRenderer> rendererList);

    void setManualNorth();
}