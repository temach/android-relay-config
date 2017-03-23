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

        final CycleControl currentCycle = ((MainApplication)getApplication()).getCurrentCycle();

        // allow currentCycle to display
        LinearLayout displayLayout = (LinearLayout) findViewById(R.id.currentEditCycle);
        displayLayout.removeAllViews();
        displayLayout.addView(currentCycle);

        // when the activity is created, the currentCycle may
        // already have some TimeStrips, so add them to LinearLayout
        // to allow editing
        LinearLayout timeStripsLayout = (LinearLayout)findViewById(R.id.timeStripsList);
        timeStripsLayout.removeAllViews();
        currentCycle.fillLayoutWithTimeStrips(timeStripsLayout);

        // when we click add time strip on screen 3
        // we create a new timeStrip control
        // add it to LinearLayout and notify CycleControl
        // that it has a new TimeStrip. CycleControl registers
        // to listen to changes in TimeStripControl and redraws itself when need.
        Button addTimeStrip = (Button) findViewById(R.id.addTimeStrip);
        addTimeStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeStripControl tm = new TimeStripControl(getBaseContext(), new RelayTimeStripData());
                currentCycle.addTimeStrip(tm);
                LinearLayout timeStrips = (LinearLayout) findViewById(R.id.timeStripsList);
                timeStrips.addView(tm);
            }
        });
    }
}
