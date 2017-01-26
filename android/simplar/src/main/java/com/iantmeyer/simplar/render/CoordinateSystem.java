package com.iantmeyer.simplar.render;

import android.opengl.Matrix;
import android.util.Log;

public interface CoordinateSystem {

    String TAG = "CoordinateSystem";

    void getModelViewMatrix(float[] modelViewMatrix, float[] eyeMatrix, float[] cameraMatrix);

    void setNorthOffset(float azimuthRadians);

    float[] IDENTITY = new float[]{
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    /*
            Open GL World Coordinates

                      (-z)
                        N
                        ^
                        |
                        |
          (-x) W <------|------> E (+x)
                        |
                        |
                        v
                        S
                      (+z)
    */
    /**
     * Open Gl World CoordinateSystem2 are
     * X: ???
     * Y: ???
     * Z: ???
     */
    CoordinateSystem OPEN_GL_WORLD = new CoordinateSystem() {
        @Override
        public void getModelViewMatrix(float[] modelViewMatrix, float[] eyeMatrix, float[] cameraMatrix) {
            Matrix.multiplyMM(modelViewMatrix, 0, eyeMatrix, 0, cameraMatrix, 0);
        }

        @Override
        public void setNorthOffset(float azimuthRadians) {
        }
    };

    /*
            East-North-Up Coordinates

                      (+y)
                        N
                        ^
                        |
                        |
          (-x) W <------|------> E (+x)
                        |
                        |
                        v
                        S
                      (-y)
    */
    /**
     * East-North-Up Coordinates are
     * X: East      (meters?)
     * Y: North     (meters?)
     * Z: Up        (meters?)
     */
    CoordinateSystem EAST_NORTH_UP = new CoordinateSystem() {
        final float[] ORIGINAL_MATRIX = new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };

        float[] mAzimuthMatrix = new float[16];
        float[] mTransformMatrix = ORIGINAL_MATRIX.clone();

        @Override
        public void getModelViewMatrix(float[] modelViewMatrix, float[] eyeMatrix, float[] cameraMatrix) {
            Matrix.multiplyMM(modelViewMatrix, 0, eyeMatrix, 0, cameraMatrix, 0);
            Matrix.multiplyMM(modelViewMatrix, 0, modelViewMatrix, 0, mTransformMatrix, 0);
        }

        public void setNorthOffset(float azimuthRadians) {
            Matrix.setRotateM(mAzimuthMatrix, 0, (float) (azimuthRadians * 180 / Math.PI), 0f, 1f, 0f);
            Matrix.multiplyMM(mTransformMatrix, 0, mAzimuthMatrix, 0, ORIGINAL_MATRIX, 0);
            Log.d(TAG, "Set Azimuth correction to " + azimuthRadians + "rads == " + azimuthRadians * 180 / Math.PI + " degs");
        }
    };

    /*
               Device Coordinates
                      (+y)
                       up
                        ^
                        |
                        |
       (-x) left <------|------> right (+x)
                        |
                        |
                        v
                       down
                       (-y)

    */
    // TODO IM: Verify/determine the units here
    /**
     * Device Coordinates
     * X: Screen Right  (TODO: units?)
     * Y: Screen Up     (TODO: units?)
     * Z: Into Screen   (TODO: units?)
     */
    CoordinateSystem NORMALIZED_DEVICE = new CoordinateSystem() {
        @Override
        public void getModelViewMatrix(float[] modelViewMatrix, float[] eyeMatrix, float[] cameraMatrix) {
            Matrix.multiplyMM(modelViewMatrix, 0, IDENTITY, 0, cameraMatrix, 0);
        }

        public void setNorthOffset(float azimuthRadians) {
        }
    };
}