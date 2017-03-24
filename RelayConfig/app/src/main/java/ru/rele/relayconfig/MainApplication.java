package ru.rele.relayconfig;

import android.app.Application;

import com.github.ik024.calendar_lib.custom.YearView;

/**
 * Created by artem on 3/22/17.
 * Use this class to pass info between activities
 */

public class MainApplication extends Application {

    private CycleControl currentCycle;
    public void setCurrentCycle(CycleControl cc) {
        currentCycle = cc;
    }

    // You should not need to ever reset CurrentCycle
    // to null because the user might "go back" to previous
    // screen and the CurrentCycle should still be active
    public CycleControl getCurrentCycle() {
        if (currentCycle == null) throw new AssertionError("CurrentCycle has not been set OR has been reset to null");
        return currentCycle;
    }

    private RelayCalendarData calendar = new RelayCalendarData();
    public RelayCalendarData getCalendar() {
        return calendar;
    }

    private YearView calendarControl;
    public YearView getCalendarControl() {
        return calendarControl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        calendarControl = new YearView(getBaseContext());
    }
}
