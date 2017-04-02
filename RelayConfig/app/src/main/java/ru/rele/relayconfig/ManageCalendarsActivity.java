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
                app.getCalendarList().add(data);
                CalendarIconControl control = new CalendarIconControl(ManageCalendarsActivity.this, data);
                calendarControlsList.addView(control, calendarControlsList.getChildCount() - 1);
            }
        });

        Button readCalendar = (Button) findViewById(R.id.gotoWifiActivity);
        readCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set current cycle to null, so we have nothing
                // to flush to device only reading is allowed
                app.nullifyCurrentCalendar();
                // Show calendar with all cycles
                Intent myIntent = new Intent(ManageCalendarsActivity.this, SelectDeviceWifiActivity.class);
                startActivity(myIntent);
            }
        });

        // speed up debug of networking
        // final RelayCalendarData calData = new RelayCalendarData();
        // app.getCalendarList().add(calData);

        // RelayCycleData cycleData = new RelayCycleData();
        // calData.addRelayCycle(cycleData);

        // RelayTimeStripData tmData = new RelayTimeStripData();
        // tmData.updateStart(1, 0);
        // tmData.updateEnd(10, 0);
        // cycleData.addTimeStrip(tmData);

        // RelayTimeStripData tmData1 = new RelayTimeStripData();
        // tmData1.updateStart(9, 30);
        // tmData1.updateEnd(23, 30);
        // cycleData.addTimeStrip(tmData1);

        // calData.cycleAddDay(cycleData, 2017, 7, 3);
        // calData.cycleAddDay(cycleData, 2017, 9, 9);

        // app.setCurrentCalendar(calData);
        // // Show calendar with all cycles
        // Intent myIntent = new Intent(ManageCalendarsActivity.this, SelectDeviceWifiActivity.class);
        // startActivity(myIntent);

    }

}
