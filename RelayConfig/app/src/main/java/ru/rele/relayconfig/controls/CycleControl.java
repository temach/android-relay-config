package ru.rele.relayconfig.controls;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import info.staticfree.android.twentyfourhour.Analog24HClock;
import ru.rele.relayconfig.CycleCalendarActivity;
import ru.rele.relayconfig.CycleEditActivity;
import ru.rele.relayconfig.CycleNameActivity;
import ru.rele.relayconfig.MainApplication;
import ru.rele.relayconfig.R;
import ru.rele.relayconfig.relaydata.RelayCycleData;
import ru.rele.relayconfig.relaydata.RelayTimeStripData;

/**
 * Created by artem on 3/22/17.
 */

public class CycleControl extends Analog24HClock implements RelayCycleData.onCycleUpdateListener {

    private RelayCycleData cycleData;

    private ClockOverlayButton calendarButton;
    private ClockOverlayButton editButton;
    private ClockOverlayButton cycleName;

    private ClockOverlayText cycleColor;
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

        cycleName.setText(cycle.cycleName);
        cycleColor.setBgPaint(cycle.cycleColor);

        timeStripManager.refillClock(cycleData.getTimeStrips());
        this.invalidate();
    }

    private void loadLayouts() {
        calendarButton = new ClockOverlayButton(-0.2f, -0.5f, 0.045f);
        calendarButton.setText("CAL");
        calendarButton.setOnClickListener(new ClockOverlayButton.OnClickListener() {
            @Override
            public void onClockOverlayButtonClick() {
                ((MainApplication)getContext().getApplicationContext()).setCurrentCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleCalendarActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });

        cycleColor = new ClockOverlayText(-0.295f, 0.2f, 0.0625f);
        // quick hack to give the rectangle some size
        cycleColor.setText("   ");

        cycleName = new ClockOverlayButton(-0.295f, -0.05f, 0.0625f);
        cycleName.setOnClickListener(new ClockOverlayButton.OnClickListener() {
            @Override
            public void onClockOverlayButtonClick() {
                ((MainApplication)getContext().getApplicationContext()).setCurrentCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleNameActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });

        editButton = new ClockOverlayButton(-0.2f, 0.5f, 0.045f);
        editButton.setText("EDIT");
        editButton.setOnClickListener(new ClockOverlayButton.OnClickListener() {
            @Override
            public void onClockOverlayButtonClick() {
                ((MainApplication)getContext().getApplicationContext()).setCurrentCycle(cycleData);
                // start intent
                Intent myIntent = new Intent(CycleControl.this.getContext(), CycleEditActivity.class);
                CycleControl.this.getContext().startActivity(myIntent);
            }
        });


        this.addDialOverlay(cycleName);
        this.addTouchOverlay(cycleName);

        this.addDialOverlay(calendarButton);
        this.addTouchOverlay(calendarButton);

        this.addDialOverlay(editButton);
        this.addTouchOverlay(editButton);

        this.addDialOverlay(cycleColor);

        // add some colors
        timeStripColors.add(Color.RED);
        timeStripColors.add(Color.MAGENTA);
        timeStripColors.add(Color.BLUE);
        // and the manager
        timeStripManager = new ClockOverlayTimeStripManager(this, timeStripColors);
    }

    @Override
    public void onCycleUpdate(RelayCycleData cycleData, RelayTimeStripData tmData, RelayCycleData.EVENT_TYPE eventType) {
        // whatever changes, just redraw the display
        if (eventType == RelayCycleData.EVENT_TYPE.ADD_TIME_STRIP) {
            timeStripManager.refillClock(cycleData.getTimeStrips());
        }
        else if (eventType == RelayCycleData.EVENT_TYPE.UPDATE_TIME_STRIP) {
            CycleControl.this.setBackgroundColor(0);
            // check that we have no overlapping cycles
            for (RelayTimeStripData tm : cycleData.getTimeStrips()) {
                if (tm != tmData && tmData.isOverlapping(tm)) {
                    CycleControl.this.setBackgroundResource(R.color.warnValue);
                }
            }
        }
        this.invalidate();
    }
}
