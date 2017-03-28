package ru.rele.relayconfig;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem on 3/22/17.
 */

public class TimeStripControl extends LinearLayout {


    public RelayTimeStripData timeStripData;

    // One view object (control) is forever tied to one data object (relay data)
    // This control is never created in XML so there is need to
    // provide a method that would assign the data like it is in the CycleControl
    public TimeStripControl(Context context, RelayTimeStripData tm) {
        super(context);
        timeStripData = tm;
        loadLayouts();
    }

    void loadLayouts() {
        inflate(getContext(), R.layout.timestrip_control, this);

        final TimePicker startPicker = (TimePicker) findViewById(R.id.startTimePicker);
        startPicker.setIs24HourView(true);
        startPicker.setCurrentHour(9);
        startPicker.setCurrentMinute(0);
        final TimePicker endPicker = (TimePicker) findViewById(R.id.endTimePicker);
        endPicker.setIs24HourView(true);
        endPicker.setCurrentHour(18);
        endPicker.setCurrentMinute(0);

        // final Drawable bgColorDefault = getBackground();

        startPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeStripData.updateStart(timePicker.getCurrentHour()
                        , timePicker.getCurrentMinute());
                // warn that the cycle which passes midnight is dangerous
                if (startPicker.getCurrentHour() < endPicker.getCurrentHour()) {
                    TimeStripControl.this.setBackgroundResource(R.color.warnValue);
                }
                else {
                    TimeStripControl.this.setBackgroundColor(0);
                }
            }
        });
        endPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeStripData.updateEnd(timePicker.getCurrentHour()
                        , timePicker.getCurrentMinute());
                // warn that the cycle which passes midnight is dangerous
                if (startPicker.getCurrentHour() < endPicker.getCurrentHour()) {
                    TimeStripControl.this.setBackgroundResource(R.color.warnValue);
                }
                else {
                    TimeStripControl.this.setBackgroundColor(0);
                }
            }
        });
    }


}
