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

    public boolean isOverlapping(RelayTimeStripData other) {
        // check other.start is included
        if (this.startHour < other.startHour && other.startHour < this.endHour)
            return true;
        // check other.end is included
        else if (this.startHour < other.endHour && other.endHour < this.endHour)
            return true;

        // check the other way around
        // check other.start is included
        if (other.startHour < this.startHour && this.startHour < other.endHour)
            return true;
        // check this.end is included
        else if (other.startHour < this.endHour && this.endHour < other.endHour)
            return true;

        return false;
    }

    public void addOnTimeStripUpdateListener(onTimeStripUpdateListener listener) {
        this.listeners.add(listener);
    }

    public void removeOnTimeStripUpdateListener(onTimeStripUpdateListener listener) {
        this.listeners.remove(listener);
    }
}
