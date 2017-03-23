package ru.rele.relayconfig;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem on 3/22/17.
 */

public class CycleControl extends LinearLayout {

    private RelayCycleData cycleData;
    private List<TimeStripControl> timeStripControls = new ArrayList<>();
    private Button calendarBtn;
    private Button editBtn;

    // These two are used in the current display model
    // they will be removed when the proper circular control will be made
    private LinearLayout startTimesLayout;
    private LinearLayout endTimesLayout;

    // One view object (control) is forever tied to one data object (relay data)
    public CycleControl(Context context, RelayCycleData data) {
        super(context);
        cycleData = data;
        loadLayouts();
    }

    // It seems that this is not used anywhere
    public CycleControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.cycle_control, this);
        loadLayouts();
    }

    void loadLayouts() {
        startTimesLayout = (LinearLayout) findViewById(R.id.startTimeRow);
        endTimesLayout = (LinearLayout) findViewById(R.id.endTimeRow);
        calendarBtn = (Button) findViewById(R.id.openCalendarForTimeCycle);
        editBtn = (Button) findViewById(R.id.editTimeCycle);

        calendarBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get ready to share quest info
                MainApplication app = (MainApplication) getContext().getApplicationContext();
                app.setCurrentCycle(CycleControl.this);
                // start intent
                Intent myIntent = new Intent(getContext(), CycleCalendarActivity.class);
                getContext().startActivity(myIntent);
            }
        });

        editBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get ready to share quest info
                MainApplication app = (MainApplication) getContext().getApplicationContext();
                app.setCurrentCycle(CycleControl.this);
                // start intent
                Intent myIntent = new Intent(getContext(), CycleEditActivity.class);
                getContext().startActivity(myIntent);
            }
        });
    }

    void fillLayoutWithTimeStrips(LinearLayout ll) {
        for (TimeStripControl tm : timeStripControls) {
            ll.addView(tm);
        }
    }

    void addTimeStrip(TimeStripControl tm) {
        cycleData.addTimeStrip(tm.timeStripData);
        timeStripControls.add(tm);
        tm.setOnTimeStripUpdateListener(new TimeStripControl.onTimeStripUpdateListener() {
            @Override
            public void onTimeStripUpdate(TimeStripControl timeStrip) {
                redrawCycleDisplay();
            }
        });
    }

    void redrawCycleDisplay() {
        // clear layouts, except for the first TextView
        startTimesLayout.removeViews(1, startTimesLayout.getChildCount() - 1);
        endTimesLayout.removeViews(1, endTimesLayout.getChildCount() - 1);

        // add all the time strips
        for (TimeStripControl ts : timeStripControls) {
            TextView start = new TextView(startTimesLayout.getContext());
            start.setText("" + ts.timeStripData.startHour);
            startTimesLayout.addView(start);
            TextView end = new TextView(endTimesLayout.getContext());
            end.setText("" + ts.timeStripData.endHour);
            endTimesLayout.addView(end);
        }
    }

}
