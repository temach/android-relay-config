package ru.rele.relayconfig;

import java.util.Calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import info.staticfree.android.twentyfourhour.overlay.DialOverlay;

/**
 * Created by artem on 3/26/17.
 */

public class ClockOverlayTimeStrip implements DialOverlay {

    private RelayTimeStripData timeStrip;

    private float ratio = 0.5f;
    private int startHour;
    private int endHour;
    private final Paint p;

    public ClockOverlayTimeStrip(RelayTimeStripData data, float balance) {
        ratio = balance;
        data.setOnTimeStripUpdateListener(new RelayTimeStripData.onTimeStripUpdateListener() {
            @Override
            public void onTimeStripUpdate(RelayTimeStripData data) {
                startHour = data.getStartHour();
                endHour = data.getEndHour();
            }
        });
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.RED);
        p.setAlpha(127);
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                       boolean sizeChanged) {
        float r = (float) (Math.min(w, h) / 2.0);
        r *= ratio;
        RectF circle = new RectF(cX - r, cY - r, cX + r, cY + r);
        float angle1 = getHourHandAngle(startHour, 0) + 90;
        float angle2 = getHourHandAngle(endHour, 0) + 90;
        float sweepAngle = Math.abs(angle2 - angle1);
        canvas.drawArc(circle, angle1, sweepAngle, false, p);
    }

    public static float getHourHandAngle(int h, int m) {
        return (h / 24.0f) * 360;
    }

}
