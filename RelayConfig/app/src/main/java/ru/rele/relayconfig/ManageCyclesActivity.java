package ru.rele.relayconfig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ManageCyclesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cycles);

        final LinearLayout cyclesList = (LinearLayout) findViewById(R.id.cyclesList);
        Button addCycleBtn = (Button) findViewById(R.id.addCycle);

        addCycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelayCycleData cycleData = new RelayCycleData();
                ((MainApplication)getApplication()).getCalendar().addRelayCycle(cycleData);
                CycleControl cc = new CycleControl(getBaseContext());
                cc.assignData(cycleData);
                cyclesList.addView(cc, 0);
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

        RelayCycleData cycleData = new RelayCycleData();
        ((MainApplication)getApplication()).getCalendar().addRelayCycle(cycleData);
        CycleControl cc = new CycleControl(getBaseContext());
        cc.assignData(cycleData);
        cyclesList.addView(cc, 0);

        ((MainApplication)getApplication()).setCycle(cycleData);
        // start intent
        Intent myIntent = new Intent(ManageCyclesActivity.this, CycleEditActivity.class);
        startActivity(myIntent);

    }
}
