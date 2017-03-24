package ru.rele.relayconfig;

import java.util.ArrayList;

/**
 * Created by artem on 3/23/17.
 */

public class RelayTimeStripData {

    public interface onTimeStripUpdateListener
    {
        void onTimeStripUpdate(RelayTimeStripData data);
    }

    private int startHour;
    private int startMinute;

    private int endHour;
    private int endMinute;

    private ArrayList<onTimeStripUpdateListener> listeners = new ArrayList<>();

    RelayTimeStripData() {
        // empty constructor
    }

    public int getStartHour() {
        return startHour;
    }
    public int getEndHour() {
        return endHour;
    }

    public void updateStart(int startH, int startM) {
        startHour = startH;
        startMinute = startM;
        for (onTimeStripUpdateListener listener : listeners) {
            listener.onTimeStripUpdate(this);
        }
    }

    public void updateEnd(int endH, int endM) {
        endHour = endH;
        endMinute = endM;
        for (onTimeStripUpdateListener listener : listeners) {
            listener.onTimeStripUpdate(this);
        }
    }

    public void setOnTimeStripUpdateListener(onTimeStripUpdateListener listener)
    {
        // Store the listener object
        this.listeners.add(listener);
    }
}
