package com.example.ayush.rentalvehicles;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by HP on 06-05-2017.
 */
public class Online
{
    Context context;
    public Online(Context context)
    {
        this.context=context;
    }
    public boolean isConnected()
    {

        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo=cm.getActiveNetworkInfo();
        if(networkinfo!=null && networkinfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
