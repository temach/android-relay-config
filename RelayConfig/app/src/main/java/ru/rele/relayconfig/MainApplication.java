package ru.rele.relayconfig;

import android.app.Application;


/**
 * Created by artem on 3/22/17.
 * Use this class to pass info between activities
 */

public class MainApplication extends Application {

    private RelayCalendarData calendar = new RelayCalendarData();
    public RelayCalendarData getCalendar() {
        return calendar;
    }

    private RelayCycleData currentCycle;
    public RelayCycleData getCurrentCycle() {
        if (currentCycle == null) throw new AssertionError("Cycle can not be null");
        return currentCycle;
    }
    public void setCurrentCycle(RelayCycleData c) {
        if (c == null) throw new AssertionError("Cycle can not be null");
        currentCycle = c;
    }

}
