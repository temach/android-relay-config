package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.ik024.calendar_lib.custom.YearView;
import com.github.ik024.calendar_lib.listeners.YearViewClickListeners;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // get calendar data
        final RelayCalendarData calendarData = ((MainApplication)getApplication()).getCalendar();

        // Now fill calendar with data
        final YearView yearCalendarControl = (YearView) findViewById(R.id.yearCalendar);
        for (int month=0; month < 12; month++) {
            yearCalendarControl.getMonthView(month).setEventList(
                    calendarData.getEventsForMonth(month)
            );
        }
    }
}
