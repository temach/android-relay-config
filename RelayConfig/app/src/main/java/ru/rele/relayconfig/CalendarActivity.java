package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.ik024.calendar_lib.listeners.YearViewClickListeners;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // get calendar data
        final RelayCalendarData calendarData = ((MainApplication)getApplication()).getCalendar();

        // Now fill calendar with data
        final CalendarControl yearCalendarControl = (CalendarControl) findViewById(R.id.yearCalendar);
        yearCalendarControl.assignCalendarData(calendarData);
    }
}
