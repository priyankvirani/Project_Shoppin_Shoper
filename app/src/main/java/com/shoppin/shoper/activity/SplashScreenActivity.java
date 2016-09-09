package com.shoppin.shoper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shoppin.shoper.R;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.utils.Utils;

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


            if (Utils.isNullOrEmpty(DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IDatabase.IMap.KEY_EMPLOYEE_ID))) {

                Intent intent = new Intent(SplashScreenActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, NavigationDrawerActivity.class);
                startActivity(intent);
                finish();
            }
            Log.e(TAG,"Employe ID : - " +DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IDatabase.IMap.KEY_EMPLOYEE_ID));
            Log.e(TAG,"Employe SUBURB ID : - " +DBAdapter.getMapKeyValueString(SplashScreenActivity.this, IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID));


        }
    }

}
