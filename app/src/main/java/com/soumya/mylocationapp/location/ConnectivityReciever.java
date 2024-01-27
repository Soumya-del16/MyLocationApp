package com.soumya.mylocationapp.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectivityReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            Toast.makeText(context, "Internet is On", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Internet is OFF", Toast.LENGTH_SHORT).show();

        }




    }


}
