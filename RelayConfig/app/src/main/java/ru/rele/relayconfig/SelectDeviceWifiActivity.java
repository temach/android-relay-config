package ru.rele.relayconfig;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.rele.relayconfig.network.RelayServerAPI;
import ru.rele.relayconfig.relaydata.RelayCalendarData;
import ru.rele.relayconfig.relaydata.RelayCycleData;

public class SelectDeviceWifiActivity extends AppCompatActivity {

    // in milliseconds
    private final int timeout = 1000;
    private final int port = 9000;
    private final String host = "192.168.1.40";

    public class GetRelayCalendarData extends AsyncTask<Void, Void, RelayCalendarData> {

        @Override
        protected RelayCalendarData doInBackground(Void... voids) {
            // set up the service
            Retrofit restAdapter = new Retrofit
                    .Builder()
                    .baseUrl(String.format("http://%s:%s", host, port + ""))
                    .addConverterFactory(GsonFactoryBuilder.buildGsonConverter())
                    .build();
            RelayServerAPI server = restAdapter.create(RelayServerAPI.class);
            Call<RelayCalendarData> request = server.getRelayCalendarData();
            try {
                Response<RelayCalendarData> response = request.execute();
                // isSuccessful is true if response code => 200 and <= 300
                if (response.isSuccessful()) {
                    return response.body();
                }
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainText.setText("Sending HTTP POST request to device.");
        }

        @Override
        protected void onPostExecute(RelayCalendarData data) {
            super.onPostExecute(data);
            mainText.setText("Retrieved calendar: " + data.calendarName);
        }
    }

    public class PostRelayCalendarData extends AsyncTask<RelayCalendarData, Void, Boolean> {
        @Override
        protected Boolean doInBackground(RelayCalendarData... relayCalendarDatas) {
            RelayCalendarData calendarData = relayCalendarDatas[0];

            // set up the service
            Retrofit restAdapter = new Retrofit
                    .Builder()
                    .baseUrl(String.format("http://%s:%s", host, port+""))
                    .addConverterFactory(GsonFactoryBuilder.buildGsonConverter())
                    .build();
            RelayServerAPI server = restAdapter.create(RelayServerAPI.class);
            Call<Void> request = server.submitRelayCalendarData(calendarData);
            try {
                // isSuccess is true if response code => 200 and <= 300
                return request.execute().isSuccessful();
            } catch (IOException e) {
                return Boolean.FALSE;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainText.setText("Sending HTTP POST request to device.");
        }

        @Override
        protected void onPostExecute(Boolean available) {
            super.onPostExecute(available);
            mainText.setText("Settings flushed: " + available);
        }
    }

    public class PingServerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                String host = strings[0];
                Socket socket = new Socket();
                InetSocketAddress address = new InetSocketAddress(host, port);
                socket.connect(address, timeout);
                socket.close();
                return Boolean.TRUE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isPingSucessful = true;
        }
    }

    // this is a boolean so access is synchronised
    private boolean isPingSucessful = false;

    private WifiManager mainWifi;
    private TextView mainText;
    private Button chooseWifiButton;
    private Button pingServerButton;
    private Button flushButton;

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
                    new PingServerTask().execute(host);
                }
                changeLayoutBasedOnWifi();
            }
        });

        flushButton = (Button) findViewById(R.id.flushButton);
        flushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServerUp()) {
                    // send HTTP
                    MainApplication app = ((MainApplication) getApplication());
                    new PostRelayCalendarData().execute(app.getCurrentCalendar());
                }
                changeLayoutBasedOnWifi();
            }
        });
    }

    private boolean hasWifiPermissions() {
        return true;
        // return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
        //         == PackageManager.PERMISSION_GRANTED
        //         && ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
        //         == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isWifiConnected() {
        return true;
        // return hasWifiPermissions()
        //        && mainWifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED
        //        && mainWifi.getConnectionInfo() != null;
    }

    private boolean isServerUp() {
        // execute ping async
        new PingServerTask().execute(host);
        // check for result
        return isPingSucessful;
        // return isWifiConnected() && isPingSucessful;
    }

    private void changeLayoutBasedOnWifi() {
        chooseWifiButton.setEnabled(true);
        if (isServerUp()) {
            mainText.setText("Server is ready");
            flushButton.setEnabled(true);
            pingServerButton.setEnabled(true);
        } else if (isWifiConnected()) {
            mainText.setText("Wifi is connected, but server is not responding");
            flushButton.setEnabled(false);
            pingServerButton.setEnabled(true);
        } else {
            mainText.setText("Please choose a wifi connection");
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    // WifiPermsAllowed = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                break;
        }
    }

}
