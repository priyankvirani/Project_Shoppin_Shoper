package com.shoppin.shopper.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.shoppin.shopper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.shoppin.shopper.utils.Utils.showSnackbarAlert;

/**
 * Created by ubuntu on 10/8/16.
 */

public class DataRequest {
    private static final String TAG = DataRequest.class.getSimpleName();

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private Context context;
    private CallBack callBack;

    private boolean showProgressDialog;
    private ProgressDialog progressDialog;

    public DataRequest(Context context) {
        this.context = context;
    }

    public static boolean hasError(Context context, String response,
                                   boolean isShowAlert) {
        try {
            Log.e(TAG, "checkError response = " + response);
            if (response != null && !response.isEmpty()) {
                Object json = new JSONTokener(response).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject mainJObject = ((JSONObject) json);
                    // Log.e(TAG, "mainJObject = " + mainJObject);
                    if (mainJObject.has(IWebService.KEY_RES_SUCCESS)) {
                        if (!mainJObject.getBoolean(IWebService.KEY_RES_SUCCESS)) {
                            if (isShowAlert) {
                                showSnackbarAlert(
                                        context,
                                        null,
                                        mainJObject
                                                .getString(IWebService.KEY_RES_MESSAGE));
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                try {
                    if (isShowAlert) {

                        showSnackbarAlert(context,
                                null,
                                context.getString(R.string.error_technichal_problem));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Return jsonobject from ws response
     *
     * @param jsonData
     * @return
     */
    public static JSONObject getJObjWebdata(String jsonData) {
        JSONObject dataJObject = null;
        try {
            dataJObject = (new JSONObject(jsonData))
                    .getJSONObject(IWebService.KEY_RES_DATA);
            // dataJObject = (new JSONObject(jsonData)).getJSONObject("root")
            // .getJSONObject("Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJObject;
    }

    /**
     * Return jsonobject from ws response
     *
     * @param jsonData
     * @return
     */

    public static Boolean checkSuccess(String jsonData) {


        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            if (!jsonObject.getBoolean(IWebService.KEY_RES_SUCCESS)) {
                Log.e(TAG,"if "+jsonObject.getBoolean(IWebService.KEY_RES_SUCCESS));
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Return jsonarray from ws response
     *
     * @param jsonData
     * @return
     */
    public static JSONArray getJArrayWebdata(String jsonData) {
        JSONArray dataJObject = null;
        try {
            dataJObject = (new JSONObject(jsonData))
                    .getJSONArray(IWebService.KEY_RES_DATA);
            // dataJObject = (new JSONObject(jsonData)).getJSONObject("root")
            // .getJSONArray("Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJObject;
    }

    public boolean isShowProgressDialog() {
        return showProgressDialog;
    }

    public void setShowProgressDialog(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
    }

//    public static abstract class CallBack {
//        public void onPreExecute() {
//            Log.d(TAG, "onPreExecute");
//        }
//
//        public void onPostExecute(String response) {
//            Log.d(TAG, "onPreExecute");
//        }
//    }

    public void execute(String url, String jsonParam, CallBack setCallBack) {
        callBack = setCallBack;
        if (isShowProgressDialog()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading ..");
            progressDialog.show();
        }

        callBack.onPreExecute();
        Log.e(TAG, "url = " + url);
        Log.e(TAG, "jsonParam = " + jsonParam);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request;
            if (jsonParam != null) {

                RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonParam);
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            } else {
                request = new Request.Builder()
                        .url(url)
                        .build();
            }

//            Response response = client.newCall(request).execute();
//            wsResponse = response.body().string();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    sendResponse(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendResponse(response.body().string());
                    } else {
                        sendResponse(null);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(null);
        }
    }

    private void sendResponse(final String response) {
        if (callBack != null) {
            if (context != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onPostExecute(response);
                        if (isShowProgressDialog() && progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }
    }

    public interface CallBack {
        void onPreExecute();

        void onPostExecute(String response);
    }
}
