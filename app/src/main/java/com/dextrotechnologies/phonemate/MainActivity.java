package com.dextrotechnologies.phonemate;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Auto start app on boot
        //checkAutoStartup();

        //check if notification service permission enabled or not
        if (!NotificationListener.isNotificationServiceEnabled(this)){
            startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }

        //start foreground service
        Intent foregroundService = new Intent(this, MainService.class);
        startService(foregroundService);
    }
}