package ru.rele.relayconfig.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ru.rele.relayconfig.MainApplication;
import ru.rele.relayconfig.ManageCyclesActivity;
import ru.rele.relayconfig.R;
import ru.rele.relayconfig.relaydata.RelayCalendarData;

/**
 * Created by artem on 4/1/17.
 */

public class CalendarIconControl extends LinearLayout {

    public RelayCalendarData calendarData;

    // One view object (control) is forever tied to one data object (relay data)
    // This control is never created in XML so there is need to
    // provide a method that would assign the data like it is in the CycleControl
    public CalendarIconControl(Context context, RelayCalendarData cal) {
        super(context);
        calendarData = cal;
        loadLayouts();
    }

    void loadLayouts() {
        inflate(getContext(), R.layout.timestrip_control, this);

        final Button calendarName = (Button) findViewById(R.id.calendarNameButton);
        calendarName.setText(calendarData.calendarName);

        // this is the layout for the dialog, show really be in its own .xml file
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);

        // prepare the builder, set actions and view
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter new name");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                calendarData.calendarName = input.getText().toString();
                calendarName.setText(calendarData.calendarName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        calendarName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // all the builder preparation was for this
                builder.show();
            }
        });

        final Button selectThisCalendar = (Button) findViewById(R.id.selectCalendarButton);
        selectThisCalendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication app = (MainApplication) getContext().getApplicationContext();
                app.setCurrentCalendar(calendarData);
                // Show calendar with all cycles
                Intent myIntent = new Intent(CalendarIconControl.this.getContext(), ManageCyclesActivity.class);
                CalendarIconControl.this.getContext().startActivity(myIntent);
            }
        });
    }
}
