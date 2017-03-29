package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CycleEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_edit);

        final RelayCycleData cycleData = ((MainApplication)getApplication()).getCurrentCycle();

        // allow currentCycle to display
        CycleControl cycleControl = (CycleControl) findViewById(R.id.currentEditCycle);
        cycleControl.assignData(cycleData);

        // when the activity is created, the currentCycle may
        // already have some TimeStrips, so add them to LinearLayout
        // to allow editing
        final LinearLayout timeStripsLayout = (LinearLayout)findViewById(R.id.timeStripsList);
        timeStripsLayout.removeAllViews();
        for (RelayTimeStripData tm : cycleData.getTimeStrips()) {
            TimeStripControl control = new TimeStripControl(getBaseContext(), tm);
            timeStripsLayout.addView(control);
        }

        // when we click add time strip on screen 3
        // we create a new timeStrip control
        // add it to LinearLayout and notify CycleControl
        // that it has a new TimeStrip. CycleControl registers
        // to listen to changes in TimeStripControl and redraws itself when need.
        Button addTimeStrip = (Button) findViewById(R.id.addTimeStrip);
        addTimeStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelayTimeStripData data = new RelayTimeStripData();
                cycleData.addTimeStrip(data);
                TimeStripControl control = new TimeStripControl(getBaseContext(), data);
                // show the new control
                timeStripsLayout.addView(control);
            }
        });
    }

}
