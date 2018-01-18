package com.justwayward.book;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Javen
 *
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            ReaderApplication.net = false;
        }else {
            ReaderApplication.net = true;
        }
    }
}