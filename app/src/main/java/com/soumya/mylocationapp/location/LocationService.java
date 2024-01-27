package com.soumya.mylocationapp.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.soumya.mylocationapp.R;

import java.util.Set;
import java.util.TreeSet;

public class LocationService extends Service {
    private LocationListener locationListener;
    private LocationManager locationManager;
    private String CLASS_NAME_TAG = LocationService.class.getName();

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "LocationUpdatesChannel";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a notification channel (required on Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Your Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification for the foreground service
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Your Service Title")
                .setContentText("Your Service Content")
                .setSmallIcon(R.mipmap.ic_launcher)  // Set your own icon
                .build();

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            // ActivityCompat.requestPermissions(LocationService.this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, LOCATION_PERMISSION_REQUEST_CODE);
        //} else {
            // Permission is already granted or not needed, proceed with startForeground()
            startForeground(NOTIFICATION_ID, notification);
       // }

        // Start the service in the foreground


        //Intialize the loc manage and listsener
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListsener();
        //check for internet permissions
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
        //Request location updates
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,0,
                    locationListener);

        }

        // Check if location services are enabled
        if (!isLocationEnabled()) {
            // Display an alert dialog to prompt the user to enable location services
            showEnableLocationDialog();
        }

        return START_STICKY;
    }

    private class MyLocationListsener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d(CLASS_NAME_TAG,"MyLocationListsener");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }

    private boolean isLocationEnabled() {
        // Check if location services are enabled
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showEnableLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location services are disabled. Please enable them.")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open location settings when the user clicks "Enable"
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
