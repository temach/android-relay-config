package ru.rele.relayconfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

    private List<RelayCycleData> cycles = new ArrayList<>();
    private Map<Date, RelayCycleData> calendar = new HashMap<>();

    public void addRelayCycle(RelayCycleData data) {
        cycles.add(data);
    }

    public List<Date> getEventsForMonth(int month) {
        List<Date> ret = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (Date key : calendar.keySet()) {
            cal.setTime(key);
            if (cal.get(Calendar.MONTH) == month) {
                ret.add(key);
            }
        }
        return Collections.unmodifiableList(ret);
    }

    public void cycleAddDay(RelayCycleData cycle, int year, int month, int day) {
        if (cycles.indexOf(cycle) < 0) throw new AssertionError("You must first add cycle, then set it as current.");
        calendar.put(getDate(year, month, day), cycle);
    }

    public void cycleAddWorkingDays(RelayCycleData cycle, int year, int month) {
        if (cycles.indexOf(cycle) < 0) throw new AssertionError("You must first add cycle, then set it as current.");
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
