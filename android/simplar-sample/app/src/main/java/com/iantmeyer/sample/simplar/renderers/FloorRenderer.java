package com.iantmeyer.sample.simplar.renderers;

import android.content.Context;

import com.iantmeyer.sample.simplar.renderers.base.LineRenderer;
import com.iantmeyer.sample.simplar.renderers.base.util.TransformUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

public class FloorRenderer extends LineRenderer {

    private static int GRID_SIZE = 80;
    private static float GRID_DIMEN = 1.0f;

    // TODO - Set height to zero, set camera height higher?
    private static final float HEIGHT = -5.0f;

    private static float[] buildCoordinates(int size, float dimen) {
        float[] coords = new float[3 * 2 * 2 * (size + 1)];
        float edge = dimen * size / 2;

        int offset = 0;
        // North-South Lines
        for (int idx = 0; idx < size + 1; idx++) {
            coords[0 + offset] = -edge + dimen * idx;
            coords[1 + offset] = -edge;
            coords[2 + offset] = 0f;
            coords[3 + offset] = -edge + dimen * idx;
            coords[4 + offset] = edge;
            coords[5 + offset] = 0f;
            offset += 6;
        }
        // East-West Lines
        for (int idx = 0; idx < size + 1; idx++) {
            coords[0 + offset] = -edge;
            coords[1 + offset] = -edge + dimen * idx;
            coords[2 + offset] = 0f;
            coords[3 + offset] = +edge;
            coords[4 + offset] = -edge + dimen * idx;
            coords[5 + offset] = 0f;
            offset += 6;
        }
        return coords;
    }

    private static short[] buildDrawOrder(int numberOfLines) {
        short[] drawOrder = new short[2 * numberOfLines];
        for (int line = 0; line < numberOfLines; line++) {
            drawOrder[0 + 2 * line] = (short) (2 * line);
            drawOrder[1 + 2 * line] = (short) (2 * line + 1);
        }
        return drawOrder;
    }

    private static final float[] COLOR = new float[]{0.2f, 0.2f, 0.2f, 0.2f};

    private static final float LINE_WIDTH = 3.0f;

    public FloorRenderer(Context context) {
        super(context,
                CoordinateSystem.EAST_NORTH_UP,
                buildCoordinates(GRID_SIZE, GRID_DIMEN),
                buildDrawOrder(2 * (GRID_SIZE + 1)),
                COLOR,
                LINE_WIDTH,
                TransformUtil.getTranslateMatrix(0, 0, HEIGHT)
        );
    }
}