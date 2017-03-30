package ru.rele.relayconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WifiActivity extends AppCompatActivity {

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    ScanResult wifiAP;
    List<String> wifiNames = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wifi);

        Button startConfig = (Button) findViewById(R.id.startConfigButton);
        startConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show calendar with all cycles
                Intent myIntent = new Intent(WifiActivity.this, ManageCyclesActivity.class);
                startActivity(myIntent);
            }
        });

        // This displays the currently selected wifi
        mainText = (TextView) findViewById(R.id.wifiStatus);

        // configure list view to display wifiNames ListArray
        final ListView wifiResults = (ListView)findViewById(R.id.wifiResults);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.wifi_result_row, wifiNames);
        wifiResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                wifiAP = wifiList.get(i);
                mainText.setText("Selected " + wifiNames.get(i));
            }
        });
        wifiResults.setAdapter(adapter);

        // Initiate wifi service manager
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Check for wifi is disabled
        if (! mainWifi.isWifiEnabled())
        {
            mainWifi.setWifiEnabled(true);
        }

        // wifi scaned value broadcast receiver
        receiverWifi = new WifiReceiver();

        // Register broadcast receiver
        // Broacast receiver will automatically call when number of wifi connections changed
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        mainText.setText("Starting Scan...");
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    // Broadcast receiver class called its receive method 
    // when number of wifi connections changed

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        @Override
        public void onReceive(Context c, Intent intent) {
            wifiList = mainWifi.getScanResults();
            // delete old results
            wifiNames.clear();
            // add current wifi
            for (ScanResult res : wifiList) {
                wifiNames.add(res.toString());
            }
            mainText.setText("Got some results!");
        }

    }
}
