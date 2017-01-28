package com.iantmeyer.sample.simplar.renderers.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.iantmeyer.simplar.render.CoordinateSystem;

public class TextRenderer extends TextureMesh {

    private static float[] VERTEX_COORDINATES = {
            // E    N    U
            -0.5f, 0.0f, 0.05f,
            0.5f, 0.0f, 0.05f,
            -0.5f, 0.0f, 0.55f,
            0.5f, 0.0f, 0.55f
    };

    private static short[] DRAW_ORDER = new short[]{
            0, 1, 2, 1, 3, 2
    };

    private static float[] TEXTURE_COORDINATES = new float[]{
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public TextRenderer(Context context, String text, CoordinateSystem coordinateSystem, float textSize,
                        float[] textColor, @Nullable float[] position) {
        super(
                context,
                coordinateSystem,
                VERTEX_COORDINATES,
                DRAW_ORDER,
                position,
                TEXTURE_COORDINATES,
                getBitmap(text, textSize, textColor)
        );
    }

    private static int getColor(float[] color) {
        int a = (int) (255 * color[3]);
        int r = (int) (255 * color[0]);
        int g = (int) (255 * color[1]);
        int b = (int) (255 * color[2]);
        return Color.argb(a, r, g, b);
    }

    private static Bitmap getBitmap(String text, float textSize, float[] textColor) {
        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextScaleX(2.0f);

        int color = getColor(textColor);
        textPaint.setColor(color);

        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        int textWidth = (int) Math.ceil(textPaint.measureText(text));
        int textHeight = (int) Math.ceil(textSize);

        // Create empty bitmap
        Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_4444);
        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);
        // draw the text
        canvas.drawText(text, 0, textHeight, textPaint);
        return bitmap;
    }
}