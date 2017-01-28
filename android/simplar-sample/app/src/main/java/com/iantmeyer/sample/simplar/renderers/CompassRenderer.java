package com.iantmeyer.sample.simplar.renderers;

import android.content.Context;

import com.iantmeyer.sample.simplar.renderers.base.MultiColorMesh;
import com.iantmeyer.sample.simplar.renderers.base.util.TransformUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

public class CompassRenderer extends MultiColorMesh {

    private static final float[] COORDS = {
            // North Arrow
            // E    N    U
            0f, 0.5f, 0f,               // North point
            -0.1f, 0f, 0f,              // West point
            0.1f, 0f, 0f,               // East point

            // South Arrow
            // E    N    U
            -0.1f, 0.0f, 0.0f,          // West point
            0.0f, -0.5f, 0.0f,          // South point
            0.1f, 0.0f, 0.0f            // East point
    };

    private static final short[] DRAW_ORDER = {
            0, 1, 2,                    // North Arrow
            3, 4, 5                     // South Arrow
    };

    private static final float[] POSITION = {
            // E    N    U
            0.0f, 0.0f, -4.5f           // Below device
    };

    private static final float[] COLORS = {
            // North Arrow
            0.8f, 0.0f, 0.0f, 1.0f,     // Red
            0.8f, 0.0f, 0.0f, 1.0f,
            0.8f, 0.0f, 0.0f, 1.0f,

            0.8f, 0.8f, 0.8f, 1.0f,     // Light Gray
            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f
    };

    /**
     * A red and white compass below the device
     */
    public CompassRenderer(Context context) {
        super(context,
                CoordinateSystem.EAST_NORTH_UP,
                COORDS,
                DRAW_ORDER,
                COLORS,
                TransformUtil.getTranslateMatrix(POSITION[0], POSITION[1], POSITION[2])
        );
    }
}