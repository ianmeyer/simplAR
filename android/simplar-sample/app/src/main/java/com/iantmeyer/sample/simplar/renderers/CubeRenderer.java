package com.iantmeyer.sample.simplar.renderers;

import android.content.Context;

import com.iantmeyer.sample.simplar.renderers.base.MultiColorMesh;
import com.iantmeyer.sample.simplar.renderers.base.util.TransformUtil;
import com.iantmeyer.simplar.render.CoordinateSystem;

public class CubeRenderer extends MultiColorMesh {

    private static final float[] POSITION = new float[]{4.0f, 12.0f, -4.5f};

    /*
               4-----------7        U
              /|          /|        ^
             / |         / |        |    N
            0 --------- 3  |        |   /
            |  |        |  |        |  /
            |  |        |  |        | /
            |  5--------|--6        |/
            | /         | /         *----------> E
            |/          |/
            1-----------2
     */
    private static final int FACES = 6;

    private static final float[][] CUBE_COORDS = {
            // E    N    U
            new float[]{-0.5f, -0.5f, 0.5f},       // 0
            new float[]{-0.5f, -0.5f, -0.5f},      // 1
            new float[]{0.5f, -0.5f, -0.5f},       // 2
            new float[]{0.5f, -0.5f, 0.5f},        // 3
            new float[]{-0.5f, 0.5f, 0.5f},        // 4
            new float[]{-0.5f, 0.5f, -0.5f},       // 5
            new float[]{0.5f, 0.5f, -0.5f},        // 6
            new float[]{0.5f, 0.5f, 0.5f}          // 7
    };

    private static float[] buildCoords() {
        int[] idx = new int[1];
        idx[0] = 0;
        float[] cube = new float[3 * 4 * FACES];

        // FRONT
        arrayPush(cube, CUBE_COORDS[0], idx);
        arrayPush(cube, CUBE_COORDS[1], idx);
        arrayPush(cube, CUBE_COORDS[2], idx);
        arrayPush(cube, CUBE_COORDS[3], idx);

        // TOP
        arrayPush(cube, CUBE_COORDS[4], idx);
        arrayPush(cube, CUBE_COORDS[0], idx);
        arrayPush(cube, CUBE_COORDS[3], idx);
        arrayPush(cube, CUBE_COORDS[7], idx);

        // BOTTOM
        arrayPush(cube, CUBE_COORDS[2], idx);
        arrayPush(cube, CUBE_COORDS[6], idx);
        arrayPush(cube, CUBE_COORDS[5], idx);
        arrayPush(cube, CUBE_COORDS[1], idx);

        // LEFT
        arrayPush(cube, CUBE_COORDS[4], idx);
        arrayPush(cube, CUBE_COORDS[5], idx);
        arrayPush(cube, CUBE_COORDS[1], idx);
        arrayPush(cube, CUBE_COORDS[0], idx);

        // RIGHT
        arrayPush(cube, CUBE_COORDS[3], idx);
        arrayPush(cube, CUBE_COORDS[2], idx);
        arrayPush(cube, CUBE_COORDS[6], idx);
        arrayPush(cube, CUBE_COORDS[7], idx);

        // BACK
        arrayPush(cube, CUBE_COORDS[7], idx);
        arrayPush(cube, CUBE_COORDS[6], idx);
        arrayPush(cube, CUBE_COORDS[5], idx);
        arrayPush(cube, CUBE_COORDS[4], idx);

        return cube;
    }

    private static void arrayPush(float[] array, float[] push, int[] offset) {
//        System.arraycopy(push, 0, array, offset[0], push.length);
        for (int idx = 0; idx < push.length; idx++) {
            array[offset[0] + idx] = push[idx];
        }
        offset[0] += push.length;
    }

    private static final short[] FACE_DRAW_ORDER = new short[]{0, 1, 2, 2, 0, 3};
    private static final int COORDS_PER_FACE = 4;

    private static short[] buildDrawOrder() {
        short[] drawOrder = new short[FACES * FACE_DRAW_ORDER.length];
        for (int face = 0; face < FACES; face++) {
            for (int idx = 0; idx < FACE_DRAW_ORDER.length; idx++) {
                drawOrder[FACE_DRAW_ORDER.length * face + idx] = (short) (COORDS_PER_FACE * face + FACE_DRAW_ORDER[idx]);
            }
        }
        return drawOrder;
    }

    private static final float[][] FACE_COLORS = {
            new float[]{0.0f, 1.0f, 0.0f, 1.0f},    // Front: Green
            new float[]{1.0f, 0.0f, 0.0f, 1.0f},    // Top: Red
            new float[]{0.0f, 0.0f, 1.0f, 1.0f},    // Bottom: Blue
            new float[]{1.0f, 1.0f, 0.0f, 1.0f},    // Left: Yellow
            new float[]{0.0f, 1.0f, 1.0f, 1.0f},    // Right: Cyan
            new float[]{1.0f, 0.0f, 1.0f, 1.0f}     // Back: Magenta
    };

    private static float[] buildColors() {
        float[] colors = new float[4 * COORDS_PER_FACE * FACES];
        int[] offset = new int[1];
        offset[0] = 0;
        for (int face = 0; face < FACES; face++) {
            for (int coord = 0; coord < COORDS_PER_FACE; coord++) {
                arrayPush(colors, FACE_COLORS[face], offset);
            }
        }
        return colors;
    }

    public CubeRenderer(Context context) {
        super(context,
                CoordinateSystem.EAST_NORTH_UP,
                buildCoords(),
                buildDrawOrder(),
                buildColors(),
                TransformUtil.getTranslateMatrix(POSITION[0], POSITION[1], POSITION[2])
        );
    }
}
