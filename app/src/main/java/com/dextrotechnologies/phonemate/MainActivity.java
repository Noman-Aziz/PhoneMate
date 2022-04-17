package com.dextrotechnologies.phonemate;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MODIFY_AUDIO_SETTINGS;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {

    public static Context MainActivityContext ;

    private static final int REQUEST_PERMISSION = 0;

    private static final String[] PERMS_TAKE={
            INTERNET,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            READ_CONTACTS,
            READ_SMS,
            SEND_SMS,
            READ_CALL_LOG,
            ACCESS_WIFI_STATE,
            CHANGE_WIFI_STATE,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION,
            RECORD_AUDIO,
            MODIFY_AUDIO_SETTINGS
    };

    private final String TAG = "MainActivity";

    // The Fused Location Provider provides access to location APIs.
    public static FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MainActivityContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Auto start app on boot
        //checkAutoStartup();

        //check if permissions enabled or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, PERMS_TAKE, 1339);
        }

        //check if notification service permission enabled or not
        if (!NotificationListener.isNotificationServiceEnabled(this)){
            startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }

        //start foreground service
        Intent foregroundService = new Intent(this, MainService.class);
        startService(foregroundService);

        //Location Manager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // User refused to grant permission.
            }
        }
    }
}