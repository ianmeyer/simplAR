package com.iantmeyer.simplar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.vr.sdk.base.GvrView2;
import com.iantmeyer.simplar.base.ArApi;
import com.iantmeyer.simplar.base.SimplarApi;
import com.iantmeyer.simplar.render.ArRenderer;
import com.iantmeyer.simplar.camera.CameraPermissionCallback;

import java.util.List;

public class ArView extends FrameLayout {

    private ArApi mArApi;

    public ArView(Context context) {
        super(context);
        init(context);
    }

    public ArView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.ar_view, this);

        GvrView2 gvrView = (GvrView2) this.findViewById(R.id.gvr_view);
        SurfaceView cameraSurfaceView = (SurfaceView) this.findViewById(R.id.camera_surface_view);

        mArApi = new SimplarApi();
        mArApi.setGvrView(gvrView);
        mArApi.setCameraSurfaceView(cameraSurfaceView);
    }

    public void startCamera(CameraPermissionCallback cameraCallback) {
        mArApi.startCamera(cameraCallback);
    }

    public void stopCamera() {
        mArApi.stopCamera();
    }

    public void setNorth() {
        mArApi.setManualNorth();
    }

    public int addRenderer(@NonNull ArRenderer renderer) {
        return mArApi.addRenderer(renderer);
    }

    public void addRendererList(@NonNull List<ArRenderer> rendererList) {
        mArApi.addRendererList(rendererList);
    }

    public void cameraPermissionGranted(String[] permissions, int[] grantResults) {
        if (!permissions[0].equals(Manifest.permission.CAMERA)) {
            return;
        }
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mArApi.startCamera(null);
    }
}