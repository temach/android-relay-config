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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;

public class SelectDeviceWifiActivity extends AppCompatActivity {

    WifiManager mainWifi;
    TextView mainText;
    Button chooseWifiButton;
    Button flushButton;

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

        flushButton = (Button) findViewById(R.id.flushButton);
        flushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! isWifiWorking()) {
                    changeLayoutBasedOnWifi();
                    return;
                }
                // send HTTP
                boolean result = isReachableByTcp("192.168.1.40", 9000, 1000);
                mainText.setText("Sending HTTP POST request to device. Result:" + result);
            }
        });

    }

    private boolean hasWifiPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isWifiWorking() {
        return hasWifiPermissions()
                && mainWifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED
                && mainWifi.getConnectionInfo() != null
                // timeout after one second
                ;
    }

    private void changeLayoutBasedOnWifi() {
        if (isWifiWorking()) {
            mainText.setText("Wifi is connected");
            chooseWifiButton.setVisibility(View.GONE);
            flushButton.setVisibility(View.VISIBLE);
        } else {
            mainText.setText("Please choose a wifi connection");
            chooseWifiButton.setVisibility(View.VISIBLE);
            flushButton.setVisibility(View.GONE);
        }
    }

    private boolean isReachableByTcp(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            InetSocketAddress address = new InetSocketAddress(host, port);
            boolean tmp = address.isUnresolved();
            String tmp1 = address.getHostName();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String tmp2 = address.getHostString();
            }
            int tmp3 = address.getPort();
            socket.connect(address, timeout);
            socket.close();
            return true;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (IllegalBlockingModeException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
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
