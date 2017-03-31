package ru.rele.relayconfig.controls;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.ik024.calendar_lib.adapters.EventInfo;
import com.github.ik024.calendar_lib.custom.YearView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.rele.relayconfig.relaydata.RelayCalendarData;
import ru.rele.relayconfig.relaydata.RelayCycleData;

/**
 * Created by artem on 3/29/17.
 */

public class CalendarControl extends YearView {

    private RelayCalendarData relayCalendarData;

    public CalendarControl(Context context) {
        super(context);
    }

    public CalendarControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Map<Date,EventInfo> adapter(Map<Date,RelayCycleData> input) {
        Map<Date,EventInfo> output = new HashMap<>();
        for (Date key : input.keySet()) {
            output.put(key, new EventInfo(input.get(key).cycleColor, Color.WHITE));
        }
        return output;
    }

    public void refreshMonth(RelayCalendarData calData, int month) {
        getMonthView(month).setEventList( adapter(calData.getEventsForMonth(month)) );
    }

    public void assignCalendarData(RelayCalendarData calData) {
        relayCalendarData = calData;

        for (int month=0; month < 12; month++) {
            getMonthView(month).setEventList( adapter(calData.getEventsForMonth(month)) );
        }
        this.invalidate();
    }

}
