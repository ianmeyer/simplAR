package com.iantmeyer.simplar.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SizeF;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.iantmeyer.simplar.base.ArApi;

import java.util.Collections;

/**
 * Displays camera preview on a a SurfaceView
 * <p>
 * 1. Start camera when {@link #mSurfaceView}'s SurfaceHolder is available<br>
 * 2. Get permission to use camera<br>
 * 3. Use CameraManager to open connection to the {@link #mCameraDevice} on {@link #mBackgroundThread}<br>
 * 4. Create CaptureSession for preview surface<br>
 * 5. Continuously submit CaptureRequest to CaptureSession with {@link #mBackgroundThread}
 */
public class CameraController {

    private static final String TAG = "CameraController";

    private final ArApi mArApi;

    private boolean mSurfaceReady = false;

    private final SurfaceView mSurfaceView;

    private final CameraManager mCameraManager;

    private final CameraCharacteristics mCameraCharacteristics;

    private CameraPermissionCallback mCameraCallback;

    private HandlerThread mBackgroundThread;

    private Handler mBackgroundHandler;

    private String mCameraId;

    private CameraDevice mCameraDevice;

    public CameraController(@NonNull SurfaceView surfaceView, @NonNull ArApi arApi) throws CameraAccessException {
        mSurfaceView = surfaceView;
        mArApi = arApi;

        Context context = mSurfaceView.getContext();
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        mCameraId = getFrontCameraId();

        mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);

        float[] cameraFov = getCameraFov();
        mArApi.setFov(cameraFov);
    }

    public void startCamera(@Nullable CameraPermissionCallback callback) throws CameraAccessException {
        // TODO IM: Will this lead the camera callback?
        mCameraCallback = callback;
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);
        if (mSurfaceReady) {
            openCamera();
        }
    }

    public void stopCamera() {
        stopBackgroundThread();
        if (mCameraDevice != null) {
            mCameraDevice.close();
        }
    }


    private float[] getCameraFov() {
        float[] fov = getCameraAspectRatios(mCameraCharacteristics);
        return fov;
    }

    private void openCamera() throws CameraAccessException {
        Context context = mSurfaceView.getContext();

        // TODO IM: Check Camera Permission code
        // TODO IM: Handle all camera permission possibilities
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (mCameraCallback != null) {
                mCameraCallback.requestCameraPermission();
            }
            return;
        }
        if (mCameraDevice != null) {
            return;
        }
        startBackgroundThread();
        mCameraManager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);

    }

    private String getFrontCameraId() throws CameraAccessException {
        for (String id : mCameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);

            int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
            // TODO - Option to select lense direction?
            if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                return id;
            }
        }
        // TODO - Throw exception?
        return "";
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        try {
            if (mBackgroundThread != null) {
                mBackgroundThread.quitSafely();
                mBackgroundThread.join();
            }
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e.getLocalizedMessage());
        }
    }

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mCaptureRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mCaptureRequestBuilder}
     */
    private CaptureRequest mCaptureRequest;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            mCameraDevice = null;
        }
    };

    private void createCameraPreviewSession() {
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface surface = mSurfaceView.getHolder().getSurface();
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            mCaptureRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(
                    Collections.singletonList(surface),
                    mCaptureCallback,
                    mBackgroundHandler
            );
        } catch (CameraAccessException e) {
            Log.e(TAG, "CameraAccessException: " + e.getLocalizedMessage());
        }
    }

    private CameraCaptureSession.StateCallback mCaptureCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            mCaptureRequest = mCaptureRequestBuilder.build();
            try {
                cameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mBackgroundHandler);
            } catch (CameraAccessException e) {
                Log.e(TAG, "CameraAccessException: " + e.getLocalizedMessage());
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            Log.e(TAG, "CameraCaptureSession Configuration Failed");
        }
    };

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mSurfaceReady = true;
            try {
                openCamera();
            } catch (CameraAccessException e) {
                Log.e(TAG, "CameraAccessException: " + e.getLocalizedMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            Log.i(TAG, "surfaceChanged: w, h = " + width + ", " + height);
            float[] fov = getCameraAspectRatios(mCameraCharacteristics);
            mArApi.setFov(fov);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }
    };

    private float[] getCameraAspectRatios(CameraCharacteristics cc) {
        float focalLength = cc.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)[0];
        SizeF size = cc.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
        float w = size.getWidth();
        float h = size.getHeight();
        float cameraHorizontalAngle = (float) (2 * Math.atan(w / (focalLength * 2)));
        float cameraVerticalAngle = (float) (2 * Math.atan(h / (focalLength * 2)));
        float cameraAspectRatio = cameraHorizontalAngle / cameraVerticalAngle;

        return new float[]{
                cameraHorizontalAngle,
                1.0f / cameraAspectRatio
        };
    }
}