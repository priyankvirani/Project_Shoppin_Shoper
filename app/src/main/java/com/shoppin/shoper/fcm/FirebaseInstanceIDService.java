package com.shoppin.shoper.fcm;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.activity.SigninActivity;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;
import com.shoppin.shoper.utils.IConstants;
import com.shoppin.shoper.utils.UniqueId;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static com.shoppin.shoper.R.id.etxPassword;
import static com.shoppin.shoper.R.id.etxSigninId;
import static com.shoppin.shoper.R.id.rlvGlobalProgressbar;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    //private static String url = Utils.SEND_PUSH_TOKEN;
    private static String TAG = FirebaseMessagingService.class.getSimpleName();

    public static String token;

    @Override
    public void onTokenRefresh() {

        token = FirebaseInstanceId.getInstance().getToken();

        Log.e("FirebaseInstanceID", "token: " + token);

        registerToken(token);
    }

    private void registerToken(String token) {


        try {

                JSONObject signOutParam = new JSONObject();
                signOutParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_ID, UniqueId.getUniqueId(FirebaseInstanceIDService.this));
                signOutParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_TOKEN, token);


                DataRequest signOutDataRequest = new DataRequest(FirebaseInstanceIDService.this);
                signOutDataRequest.execute(IWebService.DEVICE_REGISTRATION, signOutParam.toString(), new DataRequest.CallBack() {
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
