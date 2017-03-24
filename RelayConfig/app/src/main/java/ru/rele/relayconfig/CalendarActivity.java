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

        YearView yearCalendarControl = ((MainApplication) getApplication()).getCalendarControl();
        // clear the listener that was registered in CycleCalendarActivity
        yearCalendarControl.unregisterYearViewClickListener();

        // Add year calendar to layout
        LinearLayout calendarLayout = (LinearLayout) findViewById(R.id.yearCalendar);
        calendarLayout.removeAllViews();
        calendarLayout.addView(yearCalendarControl);
    }
}
