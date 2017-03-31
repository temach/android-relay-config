package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.rele.relayconfig.controls.CalendarControl;
import ru.rele.relayconfig.relaydata.RelayCalendarData;

public class CalendarOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_overview);

        // get calendar data
        final RelayCalendarData calendarData = ((MainApplication)getApplication()).getCalendar();

        // Now fill calendar with data
        final CalendarControl yearCalendarControl = (CalendarControl) findViewById(R.id.yearCalendar);
        yearCalendarControl.assignCalendarData(calendarData);
    }
}
