package ru.rele.relayconfig.relaydata;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by artem on 3/23/17.
 */

public class RelayCycleData {

    private String cycleName = "Set name";
    private int cycleColor = Color.RED;

    private List<RelayTimeStripData> timeStrips = new ArrayList<>();

    public enum EVENT_TYPE {
        ADD_TIME_STRIP
        , UPDATE_TIME_STRIP
        , CHANGE_CYCLE_NAME
        , CHANGE_CYCLE_COLOR;
    }

    public interface onCycleUpdateListener
    {
        void onCycleUpdate(RelayCycleData data, RelayTimeStripData tmData, EVENT_TYPE eventType);
    }

    private List<onCycleUpdateListener> listeners = new ArrayList<>();

    public void addOnCycleUpdateListener(onCycleUpdateListener listener) {
        this.listeners.add(listener);   // Store the listener object
    }

    public void removeOnCycleUpdateListener(onCycleUpdateListener listener) {
        this.listeners.remove(listener);
    }

    public List<RelayTimeStripData> getTimeStrips() {
        return Collections.unmodifiableList(timeStrips);
    }

    public String getCycleName() {
        return cycleName;
    }
    public void setCycleName(String name) {
        cycleName = name;
        informListeners(null, EVENT_TYPE.CHANGE_CYCLE_NAME);
    }

    public int getCycleColor() {
        return cycleColor;
    }
    public void setCycleColor(int color) {
        cycleColor = color;
        informListeners(null, EVENT_TYPE.CHANGE_CYCLE_COLOR);
    }

    public void addTimeStrip(RelayTimeStripData tm) {
        timeStrips.add(tm);
        tm.addOnTimeStripUpdateListener(new RelayTimeStripData.onTimeStripUpdateListener() {
            @Override
            public void onTimeStripUpdate(RelayTimeStripData data) {
                // tell others one of the time strips has changed
                informListeners(data, EVENT_TYPE.UPDATE_TIME_STRIP);
            }
        });
        // tell others we have a new time strip
        informListeners(tm, EVENT_TYPE.ADD_TIME_STRIP);
    }

    public void informListeners(RelayTimeStripData tmData, EVENT_TYPE type) {
        // tell others we have a new time strip
        // and everything needs to be redrawn
        for (onCycleUpdateListener listener : listeners) {
            listener.onCycleUpdate(RelayCycleData.this, tmData, type);
        }
    }


}
