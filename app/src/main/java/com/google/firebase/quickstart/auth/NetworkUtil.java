package com.google.firebase.quickstart.auth;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil extends BroadcastReceiver
{
    Context mContext;
    boolean isConnected;
    public NetworkUtil(Context mContext)
    {
        this.mContext = mContext;
    }

    public void onReceive(Context context, Intent arg1)
    {

    }

    public boolean IsConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
