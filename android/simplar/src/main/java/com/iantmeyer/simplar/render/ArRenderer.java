package com.iantmeyer.simplar.render;

import android.support.annotation.NonNull;

import javax.microedition.khronos.egl.EGLConfig;

public interface ArRenderer {
    void onSurfaceCreated(EGLConfig eglConfig);

    void onDraw(@NonNull float[] mvpMatrix);

    @NonNull CoordinateSystem getCoordinateSystem();
}