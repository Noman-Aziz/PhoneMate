package com.dextrotechnologies.phonemate;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    private static JSONObject whatsappNotifications = new JSONObject();
    private static JSONArray whatsappNotificationsContainer = new JSONArray();

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Notification Listener connected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        String app = sbn.getPackageName();

        switch (app) {
            case "com.whatsapp":
                whatsapp(sbn);
                break;
            default:
                return;
        }
    }

    public static boolean isNotificationServiceEnabled(Context c){
        String pkgName = c.getPackageName();

        final String flat = Settings.Secure.getString(c.getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name: names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void whatsapp(StatusBarNotification sbn){
        // Clearing Containers if already sent
        if (whatsappNotifications.length() != 0) {
            whatsappNotifications = new JSONObject();
            whatsappNotificationsContainer = new JSONArray();
        }


        Notification notification = sbn.getNotification();
        Bundle bundle = notification.extras;

        String from;
        String message;

        try{
            from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        }catch (ClassCastException e){
            return;
        }

        try{
            message = bundle.getString(NotificationCompat.EXTRA_TEXT);
        }catch (ClassCastException e) {
            return;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("From: " , from);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("Message: " , message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        whatsappNotificationsContainer.put(obj);
    }

    public static JSONObject GetWhatsappNotifications(){

        try {
            whatsappNotifications.put("whatsappNotifications", whatsappNotificationsContainer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return whatsappNotifications;
    }

}