package ru.rele.relayconfig;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import info.staticfree.android.twentyfourhour.Analog24HClock;

/**
 * Created by artem on 3/22/17.
 */

public class CycleControl extends LinearLayout implements RelayCycleData.onCycleUpdateListener {

    private RelayCycleData cycleData;
    private Button calendarBtn;
    private Button editBtn;
    private Analog24HClock clock;
    private ClockOverlayText cycleName;
    private ClockOverlayTimeStripManager timeStripManager;

    private List<Integer> timeStripColors = new ArrayList<>();

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
        if (cycleData != null) {
            cycleData.removeOnCycleUpdateListener(this);
        }
        cycleData = cycle;
        cycleData.addOnCycleUpdateListener(this);
        timeStripManager.refillClock(cycleData.getTimeStrips());
        clock.invalidate();
    }

    private void loadLayouts() {
        inflate(getContext(), R.layout.cycle_control, this);

        calendarBtn = (Button) findViewById(R.id.openCalendarForTimeCycle);
        editBtn = (Button) findViewById(R.id.editTimeCycle);
        clock = (Analog24HClock) findViewById(R.id.clock24hours);

        // add some colors
        timeStripColors.add(Color.RED);
        timeStripColors.add(Color.MAGENTA);
        timeStripColors.add(Color.BLUE);
        // and the manager
        timeStripManager = new ClockOverlayTimeStripManager(clock, timeStripColors);

        calendarBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainApplication)getContext().getApplicationContext()).setCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleCalendarActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });

        editBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainApplication)getContext().getApplicationContext()).setCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleEditActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public void onCycleUpdate(RelayCycleData data, RelayTimeStripData tmData, RelayCycleData.EVENT_TYPE eventType) {
        // whatever changes, just redraw the display
        if (eventType == RelayCycleData.EVENT_TYPE.ADD_TIME_STRIP) {
            timeStripManager.refillClock(data.getTimeStrips());
        }
        clock.invalidate();
    }
}
