package ru.rele.relayconfig;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;

public class ManageCyclesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cycles);

        final FlexboxLayout cyclesList = (FlexboxLayout) findViewById(R.id.cyclesList);

        final RelayCalendarData calendar = ((MainApplication)getApplication()).getCalendar();

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
                Intent myIntent = new Intent(ManageCyclesActivity.this, CalendarActivity.class);
                startActivity(myIntent);
            }
        });

        // This is for debug
        // RelayCycleData cycleData = new RelayCycleData();
        // ((MainApplication)getApplication()).getCalendar().addRelayCycle(cycleData);
        // CycleControl cc = new CycleControl(getBaseContext());
        // cc.assignData(cycleData);
        // cyclesList.addView(cc, 0);

        // ((MainApplication)getApplication()).setCurrentCycle(cycleData);
        // // start intent
        // Intent myIntent = new Intent(ManageCyclesActivity.this, CycleEditActivity.class);
        // startActivity(myIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        final FlexboxLayout cyclesList = (FlexboxLayout) findViewById(R.id.cyclesList);
        cyclesList.removeViews(0, cyclesList.getChildCount() - 1);

        final RelayCalendarData calendar = ((MainApplication)getApplication()).getCalendar();

        for (RelayCycleData cycle : calendar.getCycles()) {
            CycleControl cc = new CycleControl(ManageCyclesActivity.this);
            cc.assignData(cycle);
            cyclesList.addView(cc, 0);
        }
    }

}
