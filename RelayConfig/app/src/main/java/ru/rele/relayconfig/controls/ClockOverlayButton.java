package ru.rele.relayconfig.controls;

import android.graphics.RectF;
import android.view.MotionEvent;

import info.staticfree.android.twentyfourhour.overlay.TouchOverlay;

/**
 * Created by artem on 3/28/17.
 */

public class ClockOverlayButton extends ClockOverlayText implements TouchOverlay {

    public interface OnClickListener
    {
        void onClockOverlayButtonClick();
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listen) {
        listener = listen;
    }

    /**
     * @param offsetX       the x offset, as a value between 0.0 and 1.0, from the center.
     * @param offsetY       the y offset, as a value between 0.0 and 1.0, from the center.
     * @param textSizeScale
     */
    public ClockOverlayButton(float offsetX, float offsetY, float textSizeScale) {
        super(offsetX, offsetY, textSizeScale);
    }

    @Override
    public boolean onTouch(MotionEvent motionEvent, int w, int h) {
        if (listener == null || (motionEvent.getAction() != MotionEvent.ACTION_DOWN)) {
            return false;
        }
        RectF temp = getTodayRect();
        temp.set(temp.left - 10, temp.top - 30, temp.right + 10, temp.bottom + 30);
        if (temp.contains(motionEvent.getX(), motionEvent.getY())) {
            listener.onClockOverlayButtonClick();
            return true;
        }
        return false;
    }
}
