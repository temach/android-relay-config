package ru.rele.relayconfig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import ru.rele.relayconfig.controls.CycleControl;
import ru.rele.relayconfig.relaydata.RelayCalendarData;
import ru.rele.relayconfig.relaydata.RelayCycleData;

public class ManageCyclesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cycles);

        final FlexboxLayout cyclesList = (FlexboxLayout) findViewById(R.id.cyclesList);

        final RelayCalendarData calendar = ((MainApplication)getApplication()).getCurrentCalendar();

        for (RelayCycleData cycle : calendar.getCycles()) {
            CycleControl cc = new CycleControl(ManageCyclesActivity.this);
            cc.assignData(cycle);
            cyclesList.addView(cc, 0);
        }

        Button addCycleBtn = (Button) findViewById(R.id.addCycle);
        addCycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelayCycleData cycleData = new RelayCycleData();
                calendar.addRelayCycle(cycleData);
                CycleControl cc = new CycleControl(ManageCyclesActivity.this);
                cc.assignData(cycleData);
                cyclesList.addView(cc, cyclesList.getChildCount() - 1);
            }
        });

        Button viewCalendarBtn = (Button) findViewById(R.id.cyclesCalendar);
        viewCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show calendar with all cycles
                Intent myIntent = new Intent(ManageCyclesActivity.this, CalendarOverviewActivity.class);
                startActivity(myIntent);
            }
        });

        Button flushCalendar = (Button) findViewById(R.id.calendarFlushToDeviceButton);
        flushCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show calendar with all cycles
                Intent myIntent = new Intent(ManageCyclesActivity.this, SelectDeviceWifiActivity.class);
                startActivity(myIntent);
            }
        });

        Button viewAllCalendarsButton = (Button)findViewById(R.id.showCalendarsButton);
        viewAllCalendarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ManageCyclesActivity.this, ManageCalendarsActivity.class);
                startActivity(myIntent);
            }
        });

        // This is for debug
        // RelayCycleData cycleData = new RelayCycleData();
        // ((MainApplication)getApplication()).getCurrentCalendar().addRelayCycle(cycleData);
        // CycleControl cc = new CycleControl(getBaseContext());
        // cc.assignData(cycleData);
        // cyclesList.addView(cc, 0);

        // ((MainApplication)getApplication()).setCurrentCycle(cycleData);
        // // start intent
        // Intent myIntent = new Intent(ManageCyclesActivity.this, CycleEditActivity.class);
        // startActivity(myIntent);

    }

}
