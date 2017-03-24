package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.github.ik024.calendar_lib.custom.MonthView;
import com.github.ik024.calendar_lib.custom.YearView;
import com.github.ik024.calendar_lib.listeners.YearViewClickListeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CycleCalendarActivity extends AppCompatActivity {

    enum DAYS_SELECT_MODE {
        SINGLE_DAY
        , WORKING_DAYS
    }

    DAYS_SELECT_MODE currentMode = DAYS_SELECT_MODE.SINGLE_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_calendar);

        final CycleControl currentCycle = ((MainApplication)getApplication()).getCurrentCycle();

        // allow currentCycle to display
        LinearLayout displayLayout = (LinearLayout) findViewById(R.id.currentCalendarCycle);
        displayLayout.removeAllViews();
        displayLayout.addView(currentCycle);

        // Control radio buttons to select the mode
        final RadioButton everyDay = (RadioButton) findViewById(R.id.buttonEveryDay);
        final RadioButton workingDays = (RadioButton) findViewById(R.id.buttonWorkingDays);
        everyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // turn off other radio buttons
                workingDays.setChecked(false);
                currentMode = DAYS_SELECT_MODE.SINGLE_DAY;
            }
        });
        workingDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // turn off other radio buttons
                everyDay.setChecked(false);
                currentMode = DAYS_SELECT_MODE.WORKING_DAYS;
            }
        });

        // Put the calendar into the layout.
        final RelayCalendarData yearCalendar = ((MainApplication) getApplication()).getCalendar();
        final YearView yearCalendarControl = ((MainApplication) getApplication()).getCalendarControl();
        yearCalendarControl.registerYearViewClickListener(new YearViewClickListeners() {
            @Override
            public void dateClicked(int year, int month, int day) {
                // add to data
                if (currentMode == DAYS_SELECT_MODE.SINGLE_DAY) {
                    yearCalendar.cycleAddDay(
                            ((MainApplication) getApplication()).getCurrentCycle().getCycleData()
                            , year, month, day);
                }
                else if (currentMode == DAYS_SELECT_MODE.WORKING_DAYS) {
                    yearCalendar.cycleAddWorkingDays(
                            ((MainApplication) getApplication()).getCurrentCycle().getCycleData()
                            , year, month);
                }
                // show in control
                yearCalendarControl.getMonthView(month).setEventList(yearCalendar.getEventsForMonth(month));
            }
        });

        LinearLayout calendarLayout = (LinearLayout) findViewById(R.id.yearCalendar);
        calendarLayout.removeAllViews();
        calendarLayout.addView(yearCalendarControl);

    }

}
