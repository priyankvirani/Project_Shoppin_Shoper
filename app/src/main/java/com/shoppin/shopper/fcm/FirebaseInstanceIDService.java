package com.shoppin.shopper.fcm;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shoppin.shopper.network.DataRequest;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.UniqueId;

import org.json.JSONObject;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    //private static String url = Utils.SEND_PUSH_TOKEN;
    private static String TAG = FirebaseMessagingService.class.getSimpleName();

    public static String token;
    private Context context;

    @Override
    public void onTokenRefresh() {

        token = FirebaseInstanceId.getInstance().getToken();

        Log.e("FirebaseInstanceID", "token: " + token);

        registerToken(token);
    }

    private void registerToken(String token) {


        try {

            JSONObject tokenRefreshParam = new JSONObject();
            tokenRefreshParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_ID, UniqueId.getUniqueId(FirebaseInstanceIDService.this));
            tokenRefreshParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_TOKEN, token);


            DataRequest tokenRefreshDataRequest = new DataRequest(context);
            tokenRefreshDataRequest.execute(IWebService.DEVICE_REGISTRATION, tokenRefreshParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {

                }

                public void onPostExecute(String response) {
                    try {

                        if (!DataRequest.hasError(FirebaseInstanceIDService.this, response, false)) {


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add(IWebService.KEY_REQ_EMPLOYEE_DEVICE_ID, UniqueId.getUniqueId(FirebaseInstanceIDService.this))
//                .add(IWebService.KEY_REQ_EMPLOYEE_DEVICE_TOKEN, token)
//                .build();
//
//
//        Log.e("FirebaseInstanceID", "body: " + body.toString());
//
//
//        Request request = new Request.Builder()
//                .url(IWebService.DEVICE_REGISTRATION)
//                .post(body)
//                .build();
//
//        Log.e("FirebaseInstanceID", "request: " + request.toString());
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}
