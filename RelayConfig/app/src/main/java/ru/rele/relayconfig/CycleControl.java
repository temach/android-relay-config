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

public class CycleControl extends Analog24HClock implements RelayCycleData.onCycleUpdateListener {

    private RelayCycleData cycleData;

    private ClockOverlayButton calendarButton;
    private ClockOverlayButton editButton;

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
        cycleName.setText(cycleData.getCycleName());
        cycleData.addOnCycleUpdateListener(this);
        timeStripManager.refillClock(cycleData.getTimeStrips());
        this.invalidate();
    }

    private void loadLayouts() {
        calendarButton = new ClockOverlayButton(-0.2f, -0.35f, 0.045f);
        calendarButton.setText("CAL");
        calendarButton.setOnClickListener(new ClockOverlayButton.OnClickListener() {
            @Override
            public void onClockOverlayButtonClick() {
                ((MainApplication)getContext().getApplicationContext()).setCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleCalendarActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });

        editButton = new ClockOverlayButton(-0.2f, 0.35f, 0.045f);
        editButton.setText("EDIT");
        editButton.setOnClickListener(new ClockOverlayButton.OnClickListener() {
            @Override
            public void onClockOverlayButtonClick() {
                ((MainApplication)getContext().getApplicationContext()).setCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleEditActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });

        cycleName = new ClockOverlayText(-0.295f, -0.07f, 0.0625f);

        this.addDialOverlay(cycleName);
        this.addDialOverlay(calendarButton);
        this.addDialOverlay(editButton);
        this.addTouchOverlay(calendarButton);
        this.addTouchOverlay(editButton);

        // add some colors
        timeStripColors.add(Color.RED);
        timeStripColors.add(Color.MAGENTA);
        timeStripColors.add(Color.BLUE);
        // and the manager
        timeStripManager = new ClockOverlayTimeStripManager(this, timeStripColors);
    }

    @Override
    public void onCycleUpdate(RelayCycleData data, RelayTimeStripData tmData, RelayCycleData.EVENT_TYPE eventType) {
        // whatever changes, just redraw the display
        if (eventType == RelayCycleData.EVENT_TYPE.ADD_TIME_STRIP) {
            timeStripManager.refillClock(data.getTimeStrips());
        }
        this.invalidate();
    }
}
