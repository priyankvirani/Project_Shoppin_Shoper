package com.shoppin.shopper.intentservices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.shoppin.shopper.receivers.MyBroadCastReceiver;

/**
 * Created by ubuntu on 20/9/16.
 */

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("MyIntentService", "handling intent...");

        //Intent created for broadcasting
        Intent intentBroadCast = new Intent();

        //Filter the broadcast to the action desired
        intentBroadCast.setAction(MyBroadCastReceiver.ACTION);

        //Send the broadcast :D
        sendBroadcast(intentBroadCast);
    }
}
