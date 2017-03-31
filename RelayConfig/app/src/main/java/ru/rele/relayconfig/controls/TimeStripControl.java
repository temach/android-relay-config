package ru.rele.relayconfig.controls;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import ru.rele.relayconfig.R;
import ru.rele.relayconfig.relaydata.RelayTimeStripData;

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
        final TimePicker endPicker = (TimePicker) findViewById(R.id.endTimePicker);

        // final Drawable bgColorDefault = getBackground();

        startPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeStripData.updateStart(timePicker.getCurrentHour()
                        , timePicker.getCurrentMinute());
                // warn that the cycle which passes midnight is dangerous
                if (startPicker.getCurrentHour() > endPicker.getCurrentHour()) {
                    TimeStripControl.this.setBackgroundResource(R.color.warnValue);
                }
                else {
                    TimeStripControl.this.setBackgroundColor(Color.argb(0, 0, 0, 0));
                }
            }
        });
        endPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeStripData.updateEnd(timePicker.getCurrentHour()
                        , timePicker.getCurrentMinute());
                // warn that the cycle which passes midnight is dangerous
                if (startPicker.getCurrentHour() > endPicker.getCurrentHour()) {
                    TimeStripControl.this.setBackgroundResource(R.color.warnValue);
                }
                else {
                    TimeStripControl.this.setBackgroundColor(Color.argb(0, 0, 0, 0));
                }
            }
        });

        // set GUI values after registering the listener 
        // so that data checking is enabled
        startPicker.setIs24HourView(true);
        startPicker.setCurrentHour(9);
        startPicker.setCurrentMinute(0);

        endPicker.setIs24HourView(true);
        endPicker.setCurrentHour(18);
        endPicker.setCurrentMinute(0);
    }


}
