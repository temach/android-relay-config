package ru.rele.relayconfig.relaydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

    // This is a public field which is a quick hack.
    @SerializedName("name")
    @Expose
    public String calendarName = "Change calendar name";

    @SerializedName("calendar")
    @Expose
    private Map<Date, Integer> calendar = new HashMap<>();

    @SerializedName("cycles")
    @Expose
    private Map<Integer,RelayCycleData> cycles = new HashMap<>();


    public void addRelayCycle(RelayCycleData data) {
        cycles.put(data.getId(), data);
    }

    public Map<Date,RelayCycleData> getEventsForMonth(int month) {
        Map<Date,RelayCycleData> ret = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        for (Date key : calendar.keySet()) {
            cal.setTime(key);
            if (cal.get(Calendar.MONTH) == month) {
                ret.put(key, cycles.get(calendar.get(key)));
            }
        }
        return Collections.unmodifiableMap(ret);
    }

    public Collection<RelayCycleData> getCycles() {
        return Collections.unmodifiableCollection(cycles.values());
    }

    public void cycleAddDay(RelayCycleData cycle, int year, int month, int day) {
        if (! cycles.values().contains(cycle)) throw new AssertionError("You must first add cycle, then set it as current.");
        calendar.put(getDate(year, month, day), cycle.getId());
    }

    public void cycleRemoveDay(RelayCycleData cycle, int year, int month, int day) {
        calendar.remove(getDate(year, month, day));
    }

    public void cycleAddWorkingDays(RelayCycleData cycle, int year, int month) {
        // refer to
        // https://developer.android.com/reference/java/util/Calendar.html
        // on how to add weekends and so on
        if (! cycles.values().contains(cycle)) throw new AssertionError("You must first add cycle, then set it as current.");
        Calendar counter = getCalendarFirstDay(year, month);
        Calendar limit = (Calendar) counter.clone();
        limit.add(Calendar.MONTH, 1);
        while (counter.before(limit)) {
            if (counter.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && counter.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                // add only working days
                calendar.put(counter.getTime(), cycle.getId());
            }
            // increment counter
            counter.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void cycleRemoveWorkingDays(RelayCycleData cycle, int year, int month) {
        // refer to
        // https://developer.android.com/reference/java/util/Calendar.html
        // on how to add weekends and so on
        if (! cycles.values().contains(cycle)) throw new AssertionError("You must first add cycle, then set it as current.");
        Calendar counter = getCalendarFirstDay(year, month);
        Calendar limit = (Calendar) counter.clone();
        limit.add(Calendar.MONTH, 1);
        while (counter.before(limit)) {
            if (counter.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && counter.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                // add only working days
                calendar.remove(counter.getTime());
            }
            // increment counter
            counter.add(Calendar.DAY_OF_MONTH, 1);
        }
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

    private Calendar getCalendarFirstDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        // first set day to zero
        cal.set(Calendar.DAY_OF_MONTH, 0);
        // then set day to Monday, in the end we
        // should get first Monday of the month
        // cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

}
