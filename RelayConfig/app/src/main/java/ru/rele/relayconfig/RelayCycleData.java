package ru.rele.relayconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by artem on 3/23/17.
 */

public class RelayCycleData {

    enum EVENT_TYPE {
        ADD_TIME_STRIP
        , UPDATE_TIME_STRIP;
    }

    public interface onCycleUpdateListener
    {
        void onCycleUpdate(RelayCycleData data, RelayTimeStripData tmData, EVENT_TYPE eventType);
    }

    private List<RelayTimeStripData> timeStrips = new ArrayList<>();

    private List<onCycleUpdateListener> listeners = new ArrayList<>();

    public void setOnCycleUpdateListener(onCycleUpdateListener listener)
    {
        // Store the listener object
        this.listeners.add(listener);
    }

    public List<RelayTimeStripData> getTimeStrips() {
        return Collections.unmodifiableList(timeStrips);
    }

    public void addTimeStrip(RelayTimeStripData tm) {
        timeStrips.add(tm);
        tm.setOnTimeStripUpdateListener(new RelayTimeStripData.onTimeStripUpdateListener() {
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
