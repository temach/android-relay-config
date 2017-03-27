package ru.rele.relayconfig;

import java.util.Calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import info.staticfree.android.twentyfourhour.overlay.DialOverlay;

/**
 * Created by artem on 3/26/17.
 */

public class ClockOverlayTimeStrip implements DialOverlay, RelayTimeStripData.onTimeStripUpdateListener {

    private int startHour;
    private int endHour;

    private RelayTimeStripData timeStripData;

    private final Paint paint;

    // radius is a value in (0, 1)
    // when == 1 then will draw as far from center as possible
    // when == 0 then will draw close to the center
    private final float radius;

    public ClockOverlayTimeStrip(Paint tmPaint, float tmRadius) {
        radius = tmRadius;
        paint = tmPaint;
    }

    public void registerAsListener(RelayTimeStripData tmData) {
        if (timeStripData != null) throw new AssertionError();
        timeStripData = tmData;
        timeStripData.addOnTimeStripUpdateListener(this);
        onTimeStripUpdate(timeStripData);
    }

    public void unregisterAsListener() {
        timeStripData.removeOnTimeStripUpdateListener(this);

    }

    @Override
    public void onDraw(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                       boolean sizeChanged) {
        float r = (float) (Math.min(w, h) / 2.0);
        r *= radius;
        RectF circle = new RectF(cX - r, cY - r, cX + r, cY + r);
        float angle1 = getHourHandAngle(startHour) + 90;
        float angle2 = getHourHandAngle(endHour) + 90;
        float sweepAngle = Math.abs(angle2 - angle1);
        canvas.drawArc(circle, angle1, sweepAngle, false, paint);
    }

    private static float getHourHandAngle(int h) {
        return (h / 24.0f) * 360;
    }

    @Override
    public void onTimeStripUpdate(RelayTimeStripData data) {
        startHour = data.getStartHour();
        endHour = data.getEndHour();
    }
}
