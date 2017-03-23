package ru.rele.relayconfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by artem on 3/23/17.
 * Manages calendar data. Applies filters to months,
 * years, working days and so on.
 */

public class RelayCalendarData {

    private RelayCycleData currentCycle;
    private List<RelayCycleData> cycles = new ArrayList<>();
    private Map<Date, RelayCycleData> calendar = new HashMap<>();

    public void addRelayCycle(RelayCycleData data) {
        cycles.add(data);
    }

    public void setCurrentCycle(RelayCycleData data) {
        assert cycles.indexOf(data) >= 0 : "You must first add cycle, then set it as current.";
        currentCycle = data;
    }

    public void cycleAddDay(int year, int month, int day) {
        calendar.put(getDate(year, month, day), currentCycle);
    }

    public void cycleAddSaturdays(int year, int month) {
        // pass for now, refer to
        // https://developer.android.com/reference/java/util/Calendar.html
        // on how to add weekends and so on
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
