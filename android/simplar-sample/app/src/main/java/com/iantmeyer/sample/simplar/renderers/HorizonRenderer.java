package com.iantmeyer.sample.simplar.renderers;

import android.content.Context;

import com.iantmeyer.sample.simplar.renderers.base.LineRenderer;
import com.iantmeyer.simplar.render.CoordinateSystem;

public class HorizonRenderer extends LineRenderer {

    private static final float RADIUS = 20.0f;

    private static float[] COORDS = new float[]{
            // E  N  U
            0f, RADIUS, 0f,
            RADIUS, 0f, -0f,
            0f, -RADIUS, 0f,
            -RADIUS, 0f, 0f
    };

    private static short[] DRAW_ORDER = new short[]{0, 1, 1, 2, 2, 3, 3, 0};

    private static final float LINE_WIDTH = 5.0f;

    private static final float[] COLOR = new float[]{1.0f, 1.0f, 0.0f, 0.3f};

    /**
     * A gray line marking the horizon
     */
    public HorizonRenderer(Context context) {
        super(context,
                CoordinateSystem.EAST_NORTH_UP,
                COORDS,
                DRAW_ORDER,
                COLOR,
                LINE_WIDTH,
                null
        );
    }
}