package com.shoppin.customer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shoppin.customer.R;
import com.shoppin.customer.database.DBAdapter;
import com.shoppin.customer.utils.Utils;

import static com.shoppin.customer.database.IDatabase.IMap;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private static long SLEEP_TIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkForInternet();

    }

    private void startApp() {
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private void checkForInternet() {
        if (!Utils.isInternetAvailable(SplashScreenActivity.this, false)) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage(R.string.error_internet_check);
            alertDialogBuilder.setPositiveButton(R.string.retry,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick");
                            if (!Utils.isInternetAvailable(
                                    SplashScreenActivity.this, false)) {
                                AlertDialog alertDialog = alertDialogBuilder
                                        .create();
                                alertDialog.show();
                            } else {
                                startApp();
                            }
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

        } else {
            startApp();
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

            if (Utils.isNullOrEmpty(DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IMap.SUBURB_ID))) {
                Intent intent = new Intent(SplashScreenActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            } else {
//                if (DBAdapter.getMapKeyValueBoolean(SplashScreenActivity.this, IMap.IS_LOGIN)) {
                Intent intent = new Intent(SplashScreenActivity.this, NavigationDrawerActivity.class);
                startActivity(intent);
                finish();
            }


        }
    }

}
