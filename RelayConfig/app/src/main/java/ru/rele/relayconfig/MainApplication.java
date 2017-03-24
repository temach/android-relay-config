package ru.rele.relayconfig;

import android.app.Application;

import com.github.ik024.calendar_lib.custom.YearView;

/**
 * Created by artem on 3/22/17.
 * Use this class to pass info between activities
 */

public class MainApplication extends Application {

    private RelayCalendarData calendar = new RelayCalendarData();
    public RelayCalendarData getCalendar() {
        return calendar;
    }

    private RelayCycleData cycle;
    public RelayCycleData getCycle() {
        if (cycle == null) throw new AssertionError("Cycle can not be null");
        return cycle;
    }
    public void setCycle(RelayCycleData c) {
        if (c == null) throw new AssertionError("Cycle can not be null");
        cycle = c;
    }

}
