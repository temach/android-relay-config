package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class CycleCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_calendar);

        final CycleControl currentCycle = ((MainApplication)getApplication()).getCurrentCycle();

        // allow currentCycle to display
        LinearLayout displayLayout = (LinearLayout) findViewById(R.id.currentCalendarCycle);
        displayLayout.removeAllViews();
        displayLayout.addView(currentCycle);

        LinearLayout calendarLayout = (LinearLayout) findViewById(R.id.yearCalendar);
        displayLayout.removeAllViews();
        displayLayout.addView(currentCycle);


    }
}
