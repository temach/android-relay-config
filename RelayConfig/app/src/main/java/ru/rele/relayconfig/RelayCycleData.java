package ru.rele.relayconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by artem on 3/23/17.
 */

public class RelayCycleData {


    public interface onCycleUpdateListener
    {
        void onCycleUpdate(RelayCycleData data);
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
                informListeners();
            }
        });
        // tell others we have a new time strip
        informListeners();
    }

    public void informListeners() {
        // tell others we have a new time strip
        // and everything needs to be redrawn
        for (onCycleUpdateListener listener : listeners) {
            listener.onCycleUpdate(RelayCycleData.this);
        }
    }


}
