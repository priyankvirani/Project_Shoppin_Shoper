package com.shoppin.customer.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.shoppin.customer.R;
import com.shoppin.customer.model.Suburb;

import java.util.ArrayList;


public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // dp to px converter
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    // px to dp converter
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static boolean isNullOrEmpty(String tmpStr) {
        return !(tmpStr != null && !tmpStr.isEmpty());
    }

    public static boolean isValidEmail(String tmpStr) {
        if (isNullOrEmpty(tmpStr)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(tmpStr)
                    .matches();
        }
    }

    /**
     * Check for internet connection
     */
    public static boolean isInternetAvailable(Context context,
                                              boolean isShowAlert) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            // Toast.makeText(context, "Net", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            if (isShowAlert) {
                showToastShort(context,
                        context.getString(R.string.error_internet_check));
                // Toast.makeText(context,
                // context.getString(R.string.error_internet_check),
                // Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    /**
     * To display alerts
     *
     * @param context
     * @param title
     * @param message
     */
    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), null);

        Dialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Share with other apps
     */
    public static void share(Context context, String message) {
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        sharingIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sharingIntent,
                "Share using : "));
    }


    public static Suburb getSelectedSuburb(ArrayList<Suburb> suburbArrayList, String suburbName) {
        Suburb selectedSuburb = null;
        if (suburbArrayList != null && suburbName != null) {
            for (int i = 0; i < suburbArrayList.size(); i++) {
                if (suburbName.equals(suburbArrayList.get(i).suburb_name)) {
                    selectedSuburb = suburbArrayList.get(i);
                    break;
                }
            }
        }
        return selectedSuburb;
    }

//    public static String webServiceCall(String wsUrl, String params) {
//
//        Log.d(TAG, "MAIN_URL = " + wsUrl);
//        Log.d(TAG, "params = " + params);
//
//        String wsResponse = null;
//        try {
//            OkHttpClient client = new OkHttpClient();
//
//
//            Request request;
//            if (params != null) {
//                RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
//                request = new Request.Builder()
//                        .url(wsUrl)
//                        .post(body)
//                        .build();
//            } else {
//                request = new Request.Builder()
//                        .url(wsUrl)
//                        .build();
//            }
//
//            Response response = client.newCall(request).execute();
//            wsResponse = response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG, "wsResponse => " + wsResponse);
//        return wsResponse;
//    }


}
