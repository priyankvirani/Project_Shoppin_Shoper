package com.shoppin.shoper.activity;

import android.support.v7.app.AppCompatActivity;

import com.shoppin.shoper.utils.Utils;

public class BaseActivity extends AppCompatActivity {
//programmer21
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.isInternetAvailable(BaseActivity.this, true);
	}
}
