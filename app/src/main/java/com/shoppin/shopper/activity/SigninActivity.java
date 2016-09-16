package com.shoppin.shopper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.shoppin.shopper.R;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.network.DataRequest;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.IConstants;
import com.shoppin.shopper.utils.UniqueId;
import com.shoppin.shopper.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SigninActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.etxSigninId)
    EditText etxSigninId;

    @BindView(R.id.etxPassword)
    EditText etxPassword;

    @BindView(R.id.txtSignin)
    TextView txtSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.txtSignin)
    void singIn() {

        try {
            if (SignInValidation()) {
                JSONObject loginParam = new JSONObject();
                loginParam.put(IWebService.KEY_REQ_EMPLOYEE_MOBILE, etxSigninId.getText().toString());
                loginParam.put(IWebService.KEY_REQ_EMPLOYEE_PASSWORD, etxPassword.getText().toString());
                loginParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_TYPE, IConstants.ISignin.DEVICE_TYPE);
                loginParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_TOKEN, FirebaseInstanceId.getInstance().getToken());
                loginParam.put(IWebService.KEY_REQ_EMPLOYEE_DEVICE_ID, UniqueId.getUniqueId(SigninActivity.this));

                DataRequest signinDataRequest = new DataRequest(SigninActivity.this);
                signinDataRequest.execute(IWebService.EMPLOYEE_LOGIN, loginParam.toString(), new DataRequest.CallBack() {
                    public void onPreExecute() {
                        rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    }

                    public void onPostExecute(String response) {
                        try {
                            rlvGlobalProgressbar.setVisibility(View.GONE);
                            if (!DataRequest.hasError(SigninActivity.this, response, true)) {

                                JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                                DBAdapter.insertUpdateMap(SigninActivity.this,IDatabase.IMap.KEY_EMPLOYEE_ID,dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_ID));
                                DBAdapter.insertUpdateMap(SigninActivity.this,IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID,dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_SUBURB_ID));
                                DBAdapter.setMapKeyValueBoolean(SigninActivity.this, IDatabase.IMap.IS_LOGIN, true);

                                Intent intent = new Intent(SigninActivity.this, NavigationDrawerActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean SignInValidation() {
        boolean isValid = true;
        etxSigninId.setError(null);
        etxPassword.setError(null);
        if (Utils.isNullOrEmpty(etxSigninId.getText().toString())) {
            etxSigninId.setError(getString(R.string.error_required));
            etxSigninId.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxPassword.getText().toString())) {
            etxPassword.setError(getString(R.string.error_required));
            etxPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

}
