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
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem on 3/22/17.
 */

public class TimeStripControl extends LinearLayout {

    public interface onTimeStripUpdateListener
    {
        void onTimeStripUpdate(TimeStripControl timeStrip);
    }

    ArrayList<onTimeStripUpdateListener> listeners = new ArrayList<>();

    public RelayTimeStripData timeStripData;

    private TimePicker startPicker;
    private TimePicker endPicker;

    // One view object (control) is forever tied to one data object (relay data)
    public TimeStripControl(Context context, RelayTimeStripData tm) {
        super(context);
        timeStripData = tm;
        loadLayouts();
    }

    void loadLayouts() {
        startPicker = (TimePicker) findViewById(R.id.startTimePicker);
        endPicker = (TimePicker) findViewById(R.id.endTimePicker);

        startPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeStripData.startHour = timePicker.getCurrentHour();
                timeStripData.startMinute = timePicker.getCurrentMinute();
                for (onTimeStripUpdateListener listener : listeners) {
                    listener.onTimeStripUpdate(TimeStripControl.this);
                }
            }
        });
        endPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeStripData.endHour = timePicker.getCurrentHour();
                timeStripData.endMinute = timePicker.getCurrentMinute();
                for (onTimeStripUpdateListener listener : listeners) {
                    listener.onTimeStripUpdate(TimeStripControl.this);
                }
            }
        });
    }

    public void setOnTimeStripUpdateListener(onTimeStripUpdateListener listener)
    {
        // Store the listener object
        this.listeners.add(listener);
    }

}
