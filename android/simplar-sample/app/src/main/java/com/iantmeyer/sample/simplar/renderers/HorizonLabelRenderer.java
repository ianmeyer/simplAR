package com.iantmeyer.sample.simplar.renderers;

import android.content.Context;

import com.iantmeyer.sample.simplar.renderers.base.TextRenderer;
import com.iantmeyer.sample.simplar.renderers.base.util.TransformUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

public class HorizonLabelRenderer extends TextRenderer {

    private static final float TEXT_SIZE = 96f;

    private static final float[] TEXT_COLOR = new float[]{1.0f, 1.0f, 0.0f, 1.0f};  // Yellow

    private static final float DISTANCE = 15f;

    public HorizonLabelRenderer(Context context, String text, float azimuth, float elevation) {
        super(context,
                text,
                CoordinateSystem.EAST_NORTH_UP,
                TEXT_SIZE,
                TEXT_COLOR,
                TransformUtil.getPositionMatrix(DISTANCE, azimuth, elevation)
        );
    }
}