package com.shoppin.shopper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.shoppin.shopper.R;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.network.DataRequest;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.IConstants;
import com.shoppin.shopper.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private static long SLEEP_TIME = 2;

    @BindView(R.id.rlvContent)
    RelativeLayout rlvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(SplashScreenActivity.this);

        IntentFilter intentFilter = new IntentFilter(IConstants.UPDATE_SPLASH_SCREEN);
        // Here you can add additional actions which then would be received by the BroadcastReceiver

        broadcastManager.registerReceiver(receiver, intentFilter);

        checkForInternet();

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null && action.equals(IConstants.UPDATE_SPLASH_SCREEN)) {
                // perform your update
                checkForInternet();
            }

        }
    };

    private void startApp() {
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private void checkForInternet() {
        if (!Utils.isInternetAvailable(SplashScreenActivity.this, false)) {
            Utils.showSnackbarAlert(SplashScreenActivity.this, IConstants.UPDATE_SPLASH_SCREEN, getString(R.string.error_internet_check));
        } else {
            appVersionVerify();
        }
    }

    private class IntentLauncher extends Thread {

        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (Utils.isNullOrEmpty(DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IDatabase.IMap.KEY_EMPLOYEE_ID))) {

                Intent intent = new Intent(SplashScreenActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(SplashScreenActivity.this, NavigationDrawerActivity.class);
                startActivity(intent);
                finish();
            }
            //Log.e(TAG, "Employee ID : - " + DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IDatabase.IMap.KEY_EMPLOYEE_ID));
            //Log.e(TAG, "Employee SUBURB ID : - " + DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID));

        }
    }

    private void showAlertForAppUpdate() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(R.string.error_app_update);
        alertDialogBuilder.setPositiveButton(R.string.update,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToPlayStore();
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void goToPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void appVersionVerify() {
        JSONObject appVersionParam = new JSONObject();
        try {
            appVersionParam.put(IWebService.KEY_REQ_DEVICE_TYPE, IConstants.ISignin.DEVICE_TYPE);
            int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            appVersionParam.put(IWebService.KEY_REQ_CURRENT_APP_VERSION, versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataRequest appVersionVerifyDataRequest = new DataRequest(SplashScreenActivity.this);
        appVersionVerifyDataRequest.execute(IWebService.EMPLOYEE_APP_VERSION, appVersionParam.toString(), new DataRequest.CallBack() {
            public void onPreExecute() {
            }

            public void onPostExecute(String response) {
                try {
                    if (!DataRequest.hasError(SplashScreenActivity.this, response, true)) {
                        JSONObject dataJObject = DataRequest.getJObjWebdata(response);
                        if (dataJObject.getBoolean(IWebService.KEY_RES_UPDATE_REQUIRE)) {
                            showAlertForAppUpdate();
                        } else {
                            startApp();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
