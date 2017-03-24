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
    private Button calendarBtn;
    private Button editBtn;

    // These two are used in the current display model
    // they will be removed when the proper circular control will be made
    private LinearLayout startTimesLayout;
    private LinearLayout endTimesLayout;

    // One view object (control) is forever tied to one data object (relay data)
    public CycleControl(Context context) {
        super(context);
        loadLayouts();
    }

    // This will be used if the control is created from XML
    public CycleControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayouts();
    }

    public void assignData(RelayCycleData cycle) {
        cycleData = cycle;
        cycleData.setOnCycleUpdateListener(new RelayCycleData.onCycleUpdateListener() {
            @Override
            public void onCycleUpdate(RelayCycleData data) {
                // whatever changes, just redraw the display
                redrawCycleDisplay();
            }
        });
        // and redraw the display now as well
        redrawCycleDisplay();
    }

    private void loadLayouts() {
        inflate(getContext(), R.layout.cycle_control, this);

        startTimesLayout = (LinearLayout) findViewById(R.id.startTimeRow);
        endTimesLayout = (LinearLayout) findViewById(R.id.endTimeRow);
        calendarBtn = (Button) findViewById(R.id.openCalendarForTimeCycle);
        editBtn = (Button) findViewById(R.id.editTimeCycle);

        calendarBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleCalendarActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });

        editBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleEditActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });
    }

    public void redrawCycleDisplay() {
        // clear layouts, except for the first TextView
        startTimesLayout.removeViews(1, startTimesLayout.getChildCount() - 1);
        endTimesLayout.removeViews(1, endTimesLayout.getChildCount() - 1);

        // add all the time strips
        for (RelayTimeStripData ts : cycleData.getTimeStrips()) {
            TextView start = new TextView(startTimesLayout.getContext());
            start.setText("" + ts.getStartHour());
            startTimesLayout.addView(start);
            TextView end = new TextView(endTimesLayout.getContext());
            end.setText("" + ts.getEndHour());
            endTimesLayout.addView(end);
        }
    }

}
