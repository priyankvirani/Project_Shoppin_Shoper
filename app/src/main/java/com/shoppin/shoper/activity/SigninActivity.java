package com.shoppin.shoper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.model.Suburb;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;
import com.shoppin.shoper.utils.IConstants;
import com.shoppin.shoper.utils.UniqueId;
import com.shoppin.shoper.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

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


    private ArrayList<Suburb> suburbArrayList;
    private ArrayAdapter<Suburb> suburbArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);


        suburbArrayList = new ArrayList<>();
        suburbArrayAdapter = new ArrayAdapter<>(SigninActivity.this, android.R.layout.simple_dropdown_item_1line, suburbArrayList);

    }

    @OnClick(R.id.txtSignin)
    void singIn() {

        try {
            if (loginValidation()) {
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

                                DBAdapter.insertUpdateEmployeeData(SigninActivity.this, IDatabase.IEmployeData.KEY_EMPLOYEE_KEY,
                                        dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_ID),
                                        dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_NAME),
                                        dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_EMAIL),
                                        dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_MOBILE),
                                        dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_SUBURB_ID),
                                        false);


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

    private boolean loginValidation() {
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
