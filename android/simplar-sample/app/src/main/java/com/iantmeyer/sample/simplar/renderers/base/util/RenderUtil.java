package com.iantmeyer.sample.simplar.renderers.base.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class RenderUtil {

    private static ByteBuffer byteBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size);
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }

    public static FloatBuffer buildFloatBuffer(float[] array) {
        FloatBuffer buffer = byteBuffer(array.length * 4).asFloatBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }

    public static ShortBuffer buildShortBuffer(short[] array) {
        ShortBuffer buffer = byteBuffer(array.length * 2).asShortBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }
}