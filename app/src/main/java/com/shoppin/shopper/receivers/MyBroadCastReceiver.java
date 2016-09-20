package com.shoppin.shopper.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ubuntu on 20/9/16.
 */

public class MyBroadCastReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.shoppin.shopper.receivers";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBroadCastReceiver", "received");
        Toast.makeText(context,"Received", Toast.LENGTH_LONG).show();

    }
}
