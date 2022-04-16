package com.dextrotechnologies.phonemate;

import static com.dextrotechnologies.phonemate.MainActivity.MainActivityContext;
import static com.dextrotechnologies.phonemate.MainActivity.fusedLocationClient;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocMan {

    // Allows class to cancel the location request if it exits the activity.
    // Typically, you use one cancellation source per lifecycle.
    private static final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

    private static final String[] result = {""};

    public static String requestCurrentLocation() {


        // Request permission
        if (ActivityCompat.checkSelfPermission(
                MainActivityContext,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            // Main code
            Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.getToken()
            );

            currentLocationTask.addOnCompleteListener((new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {
                        // Task completed successfully
                        Location location = task.getResult();
                        result[0] = "Location (success): " +
                                location.getLatitude() +
                                ", " +
                                location.getLongitude();
                    } else {
                        // Task failed with an exception
                        Exception exception = task.getException();
                        result[0] = "Exception thrown: " + exception;
                    }
                }
            }));
        } else {
            // TODO: Request fine location permission
            result[0] = "Request fine location permission." ;
        }

        return result[0];
    }
}