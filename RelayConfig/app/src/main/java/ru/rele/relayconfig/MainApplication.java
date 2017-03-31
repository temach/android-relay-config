package ru.rele.relayconfig;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ru.rele.relayconfig.relaydata.RelayCalendarData;
import ru.rele.relayconfig.relaydata.RelayCycleData;


/**
 * Created by artem on 3/22/17.
 * Use this class to pass info between activities
 */

public class MainApplication extends Application {

    private List<RelayCalendarData> calendarList = new ArrayList<>();
    public List<RelayCalendarData> getCalendarList() {
        return calendarList;
    }

    private RelayCalendarData currentCalendar;
    public RelayCalendarData getCurrentCalendar() {
        if (currentCalendar == null) throw new AssertionError("Calendar can not be null");
        return currentCalendar;
    }
    public void setCurrentCalendar(RelayCalendarData cal) {
        if (cal == null) throw new AssertionError("Calendar can not be null");
        currentCalendar = cal;
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
