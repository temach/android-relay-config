package ru.rele.relayconfig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import ru.rele.relayconfig.controls.CalendarIconControl;
import ru.rele.relayconfig.relaydata.RelayCalendarData;

public class ManageCalendarsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_calendars);

        final MainApplication app = (MainApplication)getApplication();

        final FlexboxLayout calendarControlsList = (FlexboxLayout) findViewById(R.id.calendarList);

        List<RelayCalendarData> calendarList = app.getCalendarList();

        for (int i=0; i < calendarList.size(); i++) {
            final RelayCalendarData calData = calendarList.get(i);
            CalendarIconControl control = new CalendarIconControl(ManageCalendarsActivity.this, calData);
            calendarControlsList.addView(control, calendarControlsList.getChildCount() - 1);
        }

        Button addCalendar = (Button) findViewById(R.id.addCalendar);
        addCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RelayCalendarData data = new RelayCalendarData();
                data.calendarName = "new calendar";
                app.getCalendarList().add(data);
                CalendarIconControl control = new CalendarIconControl(ManageCalendarsActivity.this, data);
                calendarControlsList.addView(control, calendarControlsList.getChildCount() - 1);
            }
        });

    }

}
