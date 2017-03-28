package ru.rele.relayconfig;

import info.staticfree.android.twentyfourhour.overlay.DialOverlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by artem on 3/27/17.
 */

public class ClockOverlayText implements DialOverlay {
    public static final float ROUNDED_RECT_RADIUS = 3f;
    private final float mOffsetY;
    private final float mOffsetX;
    private static final float RECT_RATIO = 5.2f;
    private final RectF todayRect = new RectF();
    private final Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float mTextSizeScale;
    private String text;

    /**
     * @param offsetX the x offset, as a value between 0.0 and 1.0, from the center.
     * @param offsetY the y offset, as a value between 0.0 and 1.0, from the center.
     */
    public ClockOverlayText(final float offsetX, final float offsetY, final float textSizeScale) {
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        mTextSizeScale = textSizeScale;
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.argb(255, 80, 80, 80));

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.argb(192, 255, 255, 255));
    }

    public void setText(String textNew) {
        text = textNew;
    }

    // This is the background rectangle
    public RectF getTodayRect() {
        return todayRect;
    }

    @Override
    public void onDraw(final Canvas canvas, final int cX, final int cY, final int w, final int h,
                       final Calendar calendar, final boolean sizeChanged) {
        final float offsetX = w / 2 * mOffsetX;
        final float offsetY = h / 2 * mOffsetY;
        final float textSize = mTextSizeScale * w;

        mTextPaint.setTextSize(textSize);
        todayRect.set(cX + offsetX
                , cY + offsetY
                , cX + offsetX + textSize * RECT_RATIO,
                cX + offsetY + textSize);
        // Main date.
        drawTextRectBg(canvas, text, todayRect, mBgPaint, mTextPaint);
    }

    /**
     * Draws the given text with a rounded rectangle background.
     *
     * @param canvas    the canvas to draw on.
     * @param text      the text to draw.
     * @param bgSize    the size and position of the rectangle.
     * @param bgPaint   the background color.
     * @param textPaint the text color and style; the text size will be adjusted.
     */
    private void drawTextRectBg(final Canvas canvas, final CharSequence text, final RectF bgSize,
                                final Paint bgPaint, final Paint textPaint) {
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawRoundRect(bgSize, ROUNDED_RECT_RADIUS, ROUNDED_RECT_RADIUS, bgPaint);
        textPaint.setTextSize(bgSize.height());
        canvas.drawText(text, 0, text.length(), bgSize.centerX()
                , bgSize.bottom - bgSize.height() * 0.15f, textPaint);
    }

}
