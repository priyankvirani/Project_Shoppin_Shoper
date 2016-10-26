package com.shoppin.shopper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppin.shopper.R;
import com.shoppin.shopper.model.Suburb;

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
    public static boolean isInternetAvailable(Context context,boolean isShowAlert) {
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
    public static void showSnackbarAlert(final Context context, final String title, final String message) {
        if (context instanceof Activity) {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
            String btnString = null;
            if (title.equals(IConstants.UPDATE_SPLASH_SCREEN) ||
                    title.equals(IConstants.UPDATE_ORDER_ON_GOING) ||
                    title.equals(IConstants.UPDATE_ORDER_REQUEST) ||
                    title.equals(IConstants.UPDATE_ORDER_HISTORY)) {
                btnString = context.getString(R.string.retry);
            } else {
                btnString = context.getString(R.string.ok);
            }

            final Snackbar snackBar = Snackbar.make(viewGroup, message, Snackbar.LENGTH_INDEFINITE);

            snackBar.setAction(btnString, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isInternetAvailable(context, false)) {
                        snackBar.dismiss();
                    } else {
                        Utils.showSnackbarAlert(context, title, message);
                    }
                    upDateList(context, title);

                }
            });

            snackBar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackBar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackBar.show();
        }


    }

    public static void upDateList(Context context, String title) {

        if (title.equals(IConstants.UPDATE_SPLASH_SCREEN)) {
            //Log.e(TAG, "equal true");
            Intent intent = new Intent(IConstants.UPDATE_SPLASH_SCREEN);
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            broadcastManager.sendBroadcast(intent);
        } else if (title.equals(IConstants.UPDATE_ORDER_ON_GOING)) {
            //Log.e(TAG, "equal true");
            Intent intent = new Intent(IConstants.UPDATE_ORDER_ON_GOING);
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            broadcastManager.sendBroadcast(intent);
        } else if (title.equals(IConstants.UPDATE_ORDER_REQUEST)) {
            //Log.e(TAG, "equal true");
            Intent intent = new Intent(IConstants.UPDATE_ORDER_REQUEST);
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            broadcastManager.sendBroadcast(intent);
        } else if (title.equals(IConstants.UPDATE_ORDER_HISTORY)) {
            //Log.e(TAG, "equal true");
            Intent intent = new Intent(IConstants.UPDATE_ORDER_HISTORY);
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            broadcastManager.sendBroadcast(intent);
        }
    }

    public static void showToastShort1(Context context, String message) {
        if (context instanceof Activity) {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
            Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }
    }

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

    public static void showSnackbar(Activity activity, String displayText) {
/*
        Snackbar.with(activity.getApplicationContext()) // context
                .text(displayText) // text to display
                .show(activity); // activity where it is displayed*/
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
