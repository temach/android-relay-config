package ru.rele.relayconfig;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import info.staticfree.android.twentyfourhour.Analog24HClock;

/**
 * Created by artem on 3/27/17.
 */

public class ClockOverlayTimeStripManager {

    private final float minRadius = 0.5f;
    private final float maxRadius = 0.9f;
    private final int strokeWidth = 2;
    private final int alphaTransparency = 127;

    private List<Integer> colors;
    private Analog24HClock clock;

    private List<ClockOverlayTimeStrip> overlays = new ArrayList<>();
    private float radiusStep;

    public ClockOverlayTimeStripManager(Analog24HClock clock24, List<Integer> penColors) {
        colors = penColors;
        clock = clock24;
    }

    private void reconfigure(int numberOfStrips) {
        float radiusDelta = maxRadius - minRadius;
        radiusStep = radiusDelta / numberOfStrips;
    }

    private float getNextRadius() {
        return minRadius + radiusStep * overlays.size();
    }

    private Paint getNextPaint() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        int nextColorIndex = overlays.size() % colors.size();
        p.setColor(colors.get(nextColorIndex));
        p.setAlpha(alphaTransparency);
        p.setStrokeWidth(strokeWidth);
        p.setStyle(Paint.Style.STROKE);
        return p;
    }

    private void deleteOverlays() {
        for (ClockOverlayTimeStrip over : overlays) {
            // remove the individual listeners
            over.unregisterAsListener();
            // remove them from clock. So we dont affect non TimeStrip overlays
            clock.removeDialOverlay(over);
        }
        overlays.clear();
    }

    private void makeOverlays(List<RelayTimeStripData> tm) {
        reconfigure(tm.size());
        for (RelayTimeStripData data : tm) {
            ClockOverlayTimeStrip over = new ClockOverlayTimeStrip(getNextPaint(), getNextRadius());
            over.registerAsListener(data);
            clock.addDialOverlay(over);
            overlays.add(over);
        }
    }

    public void refillClock(List<RelayTimeStripData> tmData) {
        deleteOverlays();
        makeOverlays(tmData);
    }

}
