package ru.rele.relayconfig;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectDeviceWifiActivity extends AppCompatActivity {

    TextView mainText;
    WifiManager mainWifi;

    // details for getting wifi permission
    public final int MY_PERMISSIONS_REQUEST_WIFI = 99;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_device_wifi);

        // Initiate wifi service manager
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Button chooseWifi = (Button) findViewById(R.id.chooseWifiButton);
        chooseWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        Button flush = (Button) findViewById(R.id.flushButton);
        flush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainWifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED
                        && mainWifi.getConnectionInfo() != null) {
                    // Show calendar with all cycles
                    // send HTTP
                    mainText.setText("Sending HTTP POST request to device");
                    return;
                }
                else {
                    mainText.setText("Select a WiFi to conenct to the device");
                }
            }
        });

        // This displays the currently selected wifi
        mainText = (TextView) findViewById(R.id.wifiStatus);
        mainText.setText("Choose device");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (! hasWifiPermissions()) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_WIFI_STATE
                        , Manifest.permission.CHANGE_WIFI_STATE
                        , Manifest.permission.ACCESS_COARSE_LOCATION}
                , MY_PERMISSIONS_REQUEST_WIFI);
            return;
        }
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WIFI:
                for (int i=0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        return;
                    }
                }
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    // WifiPermsAllowed = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                break;
        }
    }

    private boolean hasWifiPermissions() {
        int tmp1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        return tmp1
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }
}
