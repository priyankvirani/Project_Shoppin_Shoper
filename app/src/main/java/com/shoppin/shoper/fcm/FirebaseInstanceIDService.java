package com.shoppin.shoper.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    //private static String url = Utils.SEND_PUSH_TOKEN;

    public static String token ;

    @Override
    public void onTokenRefresh() {

         token = FirebaseInstanceId.getInstance().getToken();

        Log.e("FirebaseInstanceID", "token: "+token);

        registerToken(token);
    }

    private void registerToken(String token) {
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("push_token",token)
//                .add("device_id", Utils.DeviceID(FirebaseInstanceIDService.this))
//                .add("device_type", "android")
//                .build();
//
//
//        Log.e("FirebaseInstanceID", "body: "+body.toString());
//
//
//
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        Log.e("FirebaseInstanceID", "request: "+request.toString());
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
