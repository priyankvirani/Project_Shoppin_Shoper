package com.shoppin.shopper.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.utils.IConstants;
import com.shoppin.shopper.utils.Utils;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = FirebaseMessagingService.class.getSimpleName();
    private Context mContext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getData().get("message"));
        Log.e(TAG, "title: " + remoteMessage.getData().get("title"));
        if (!Utils.isNullOrEmpty(DBAdapter.getMapKeyValueString(FirebaseMessagingService.this, IDatabase.IMap.KEY_EMPLOYEE_ID))) {
            showNotification(remoteMessage.getData().get("message"));

            Intent intent = new Intent(IConstants.UPDATE_ORDER_REQUEST);
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(FirebaseMessagingService.this);
            broadcastManager.sendBroadcast(intent);

        }

    }

    private void showNotification(String message) {

        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NavigationDrawerActivity.IS_REQUEST_NOTIFICATION, true);
        //intent.putExtra("URL", url);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Shopper")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(defaultSoundUri).setContentText(message)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setColor(getResources().getColor(R.color.app_theme_1));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        }

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }


}
