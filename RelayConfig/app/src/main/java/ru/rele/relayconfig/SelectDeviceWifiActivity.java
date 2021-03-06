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

import ru.rele.relayconfig.network.NetworkTaskInfo;
import ru.rele.relayconfig.network.TaskGetRelayCalendarData;
import ru.rele.relayconfig.network.TaskPingServer;
import ru.rele.relayconfig.network.TaskPostRelayCalendarData;

public class SelectDeviceWifiActivity extends AppCompatActivity {

    // this is a boolean so access is synchronised
    private boolean isPingSucessful = false;

    private WifiManager mainWifi;
    private TextView mainText;
    private Button chooseWifiButton;
    private Button pingServerButton;
    private Button flushButton;
    private Button readButton;

    // details for getting wifi permission
    public final int MY_PERMISSIONS_REQUEST_WIFI = 99;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device_wifi);

        // Initiate wifi service manager
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // This displays the currently selected wifi
        mainText = (TextView) findViewById(R.id.wifiStatus);

        chooseWifiButton = (Button) findViewById(R.id.chooseWifiButton);
        chooseWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        pingServerButton = (Button) findViewById(R.id.pingServerButton);
        pingServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWifiConnected()) {
                    NetworkTaskInfo netTask = new NetworkTaskInfo();
                    netTask.setListener(new NetworkTaskInfo.NetworkTaskListener() {
                        @Override
                        public void OnNetworkTaskUpdate(NetworkTaskInfo task) {
                            mainText.setText(task.getStatus());
                            isPingSucessful = task.getPingResult();
                        }
                    });
                    new TaskPingServer(netTask).execute();
                }
                changeLayoutBasedOnWifi();
            }
        });

        readButton = (Button) findViewById(R.id.readDeviceButton);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send HTTP
                NetworkTaskInfo netTask = new NetworkTaskInfo();
                netTask.setListener(new NetworkTaskInfo.NetworkTaskListener() {
                    @Override
                    public void OnNetworkTaskUpdate(NetworkTaskInfo task) {
                        mainText.setText(task.getStatus());
                        if (task.getRelayCalendarData() != null) {
                            MainApplication app = ((MainApplication) getApplication());
                            // add the retreived calendar to calendar list
                            app.getCalendarList().add(task.getRelayCalendarData());
                        }
                    }
                });
                new TaskGetRelayCalendarData(netTask).execute();
            }
        });

        flushButton = (Button) findViewById(R.id.flushDeviceButton);
        flushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send HTTP
                NetworkTaskInfo netTask = new NetworkTaskInfo();
                netTask.setListener(new NetworkTaskInfo.NetworkTaskListener() {
                                @Override
                                public void OnNetworkTaskUpdate(NetworkTaskInfo task) {
                        mainText.setText(task.getStatus());
                }
            });
                MainApplication app = ((MainApplication) getApplication());
                new TaskPostRelayCalendarData(netTask).execute(app.getCurrentCalendar());
            }
        });

        Button doneButton = (Button)findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SelectDeviceWifiActivity.this, ManageCalendarsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private boolean hasWifiPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isWifiConnected() {
        return hasWifiPermissions()
               && mainWifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED
               && mainWifi.getConnectionInfo() != null;
    }

    private boolean isServerUp() {
        // execute ping async
        if (! isWifiConnected()) {
            return false;
        }
        NetworkTaskInfo netTask = new NetworkTaskInfo();
        netTask.setListener(new NetworkTaskInfo.NetworkTaskListener() {
            @Override
            public void OnNetworkTaskUpdate(NetworkTaskInfo task) {
                mainText.setText(task.getStatus());
                isPingSucessful = task.getPingResult();
            }
        });
        new TaskPingServer(netTask).execute();
        // check for result
        return isPingSucessful;
    }

    private void changeLayoutBasedOnWifi() {
        chooseWifiButton.setEnabled(true);
        if (isServerUp()) {
            readButton.setEnabled(true);
            if (((MainApplication) getApplication()).isCalendarNull()) {
                flushButton.setEnabled(false);
            } else {
                flushButton.setEnabled(true);
            }
            pingServerButton.setEnabled(true);
        } else if (isWifiConnected()) {
            readButton.setEnabled(false);
            flushButton.setEnabled(false);
            pingServerButton.setEnabled(true);
        } else {
            mainText.setText("Please choose a wifi connection");
            readButton.setEnabled(false);
            flushButton.setEnabled(false);
            pingServerButton.setEnabled(false);
        }
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
        changeLayoutBasedOnWifi();
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
                // If task is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    // WifiPermsAllowed = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                break;
        }
    }

}
