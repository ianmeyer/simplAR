package com.iantmeyer.sample.simplar.renderers.base.util;

import android.opengl.Matrix;

public class TransformUtil {

    public static float[] getPositionMatrix(float distance, float azimuth, float elevation) {

        // TODO IM: simplify
        float[] elevationRotation = new float[16];
        Matrix.setIdentityM(elevationRotation, 0);
        Matrix.rotateM(elevationRotation, 0, elevation * 180 / (float) Math.PI, 1, 0, 0);

        float[] azimuthRotation = new float[16];
        Matrix.setIdentityM(azimuthRotation, 0);
        Matrix.rotateM(azimuthRotation, 0, -azimuth * 180 / (float) Math.PI, 0, 0, 1);

        float[] positionMatrix = new float[16];
        Matrix.setIdentityM(positionMatrix, 0);
        Matrix.multiplyMM(positionMatrix, 0, elevationRotation, 0, positionMatrix, 0);
        Matrix.multiplyMM(positionMatrix, 0, azimuthRotation, 0, positionMatrix, 0);
        Matrix.translateM(positionMatrix, 0, 0f, distance, 0f);

        return positionMatrix;
    }

    public static float[] getRotationMatrix(float azimuth, float elevation) {
        // TODO IM: simplify
        float[] elevationRotation = new float[16];
        Matrix.setIdentityM(elevationRotation, 0);
        Matrix.rotateM(elevationRotation, 0, elevation * 180 / (float) Math.PI, 1, 0, 0);

        float[] azimuthRotation = new float[16];
        Matrix.setIdentityM(azimuthRotation, 0);
        Matrix.rotateM(azimuthRotation, 0, -azimuth * 180 / (float) Math.PI, 0, 0, 1);

        float[] rotationMatrix = new float[16];
        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.multiplyMM(rotationMatrix, 0, elevationRotation, 0, rotationMatrix, 0);
        Matrix.multiplyMM(rotationMatrix, 0, azimuthRotation, 0, rotationMatrix, 0);
        return rotationMatrix;
    }

    public static float[] getTranslateMatrix(float x, float y, float z) {
        // TODO IM: simplify
        float[] transform = new float[16];
        Matrix.setIdentityM(transform, 0);
        Matrix.translateM(transform, 0, x, y, z);
        return transform;
    }
}