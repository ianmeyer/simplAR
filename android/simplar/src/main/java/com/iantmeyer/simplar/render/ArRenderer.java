package com.iantmeyer.simplar.render;

import javax.microedition.khronos.egl.EGLConfig;

public interface ArRenderer {
    void onSurfaceCreated(EGLConfig eglConfig);

    void onDraw(float[] mvpMatrix);

    CoordinateSystem getCoordinateSystem();
}