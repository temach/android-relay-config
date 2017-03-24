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
}
