package ru.rele.relayconfig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        , REMOVE_SINGLE_DAY
        , REMOVE_WORKING_DAYS
    }

    DAYS_SELECT_MODE currentMode = DAYS_SELECT_MODE.SINGLE_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_calendar);

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

        // get cycle info
        final RelayCycleData cycleData = ((MainApplication)getApplication()).getCycle();

        // allow currentCycle to display
        CycleControl cycleControl = (CycleControl) findViewById(R.id.currentCalendarCycle);
        cycleControl.assignData(cycleData);

        // get calendar data
        final RelayCalendarData calendarData = ((MainApplication)getApplication()).getCalendar();

        // Now fill calendar with data
        final YearView yearCalendarControl = (YearView) findViewById(R.id.oneCycleYearCalendar);
        for (int month=0; month < 12; month++) {
            yearCalendarControl.getMonthView(month).setEventList(
                    calendarData.getEventsForMonth(month)
            );
        }

        // Configure calendar to react to user input
        yearCalendarControl.registerYearViewClickListener(new YearViewClickListeners() {
            @Override
            public void dateClicked(int year, int month, int day) {
                // add to data
                if (currentMode == DAYS_SELECT_MODE.SINGLE_DAY) {
                    calendarData.cycleAddDay(cycleData, year, month, day);
                }
                else if (currentMode == DAYS_SELECT_MODE.REMOVE_SINGLE_DAY) {
                    calendarData.cycleRemoveDay(cycleData, year, month, day);
                }
                else if (currentMode == DAYS_SELECT_MODE.WORKING_DAYS) {
                    calendarData.cycleAddWorkingDays(cycleData, year, month);
                }
                else if (currentMode == DAYS_SELECT_MODE.REMOVE_WORKING_DAYS) {
                    calendarData.cycleRemoveWorkingDays(cycleData, year, month);
                }
                // show in control
                yearCalendarControl.getMonthView(month).setEventList(calendarData.getEventsForMonth(month));
            }
        });

        Button applyCalendar = (Button)findViewById(R.id.applyCalendar);
        applyCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CycleCalendarActivity.this, CalendarActivity.class);
                startActivity(myIntent);
            }
        });

    }

}
