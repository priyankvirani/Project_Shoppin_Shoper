package com.shoppin.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.customer.R;
import com.shoppin.customer.database.DBAdapter;
import com.shoppin.customer.database.IDatabase;
import com.shoppin.customer.model.Suburb;
import com.shoppin.customer.network.DataRequest;
import com.shoppin.customer.network.IWebService;
import com.shoppin.customer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shoppin.customer.utils.Utils.getSelectedSuburb;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.etxName)
    EditText etxName;
    @BindView(R.id.etxSigninId)
    EditText etxSigninId;
    @BindView(R.id.etxStreet)
    EditText etxStreet;
    @BindView(R.id.atxSuburb)
    AutoCompleteTextView etxSuburb;
    @BindView(R.id.etxPostcode)
    EditText etxPostcode;
    @BindView(R.id.etxPassword)
    EditText etxPassword;
    @BindView(R.id.etxConfirmPassword)
    EditText etxConfirmPassword;

    @BindView(R.id.txtSignin)
    TextView txtSignIn;
    @BindView(R.id.txtSignup)
    TextView txtSignup;
    @BindView(R.id.txtGuest)
    TextView txtGuest;

    private ArrayList<Suburb> suburbArrayList;
    private ArrayAdapter<Suburb> suburbArrayAdapter;
    private Suburb selectedSuburb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        suburbArrayList = new ArrayList<>();
        suburbArrayAdapter = new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_dropdown_item_1line, suburbArrayList);
        etxSuburb.setAdapter(suburbArrayAdapter);
        getSuburbs();
    }

    @OnClick(R.id.txtSignup)
    void singUp() {
        if (signupValidation()) {
            DataRequest signupDataRequest = new DataRequest(SignupActivity.this);
            JSONObject signupParam = new JSONObject();
            try {
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_NAME, etxName.getText().toString());
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_MOBILE, etxSigninId.getText().toString());
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_STREET, etxStreet.getText().toString());
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_SUBURB_ID, selectedSuburb.suburb_id);
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_POSTCODE, etxPostcode.getText().toString());
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_PASSWORD, etxPassword.getText().toString());

                signupParam.put(IWebService.KEY_REQ_CUSTOMER_DEVICE_TYPE, etxSigninId.getText().toString());
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_DEVICE_TOKEN, etxSigninId.getText().toString());
                signupParam.put(IWebService.KEY_REQ_CUSTOMER_DEVICE_ID, etxSigninId.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            signupDataRequest.execute(IWebService.CUSTOMER_REGISTRATION, signupParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    Log.d(TAG, "onPreExecute");
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    Log.d(TAG, "response = " + response);
                    if (!DataRequest.hasError(SignupActivity.this, response, true)) {
                        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private boolean signupValidation() {
        boolean isValid = true;
        etxName.setError(null);
        etxSigninId.setError(null);
        etxStreet.setError(null);
        etxSuburb.setError(null);
        etxPostcode.setError(null);
        etxPassword.setError(null);
        etxConfirmPassword.setError(null);

        selectedSuburb = Utils.getSelectedSuburb(suburbArrayList, etxSuburb.getText().toString());

        if (Utils.isNullOrEmpty(etxName.getText().toString())) {
            etxName.setError(getString(R.string.error_required));
            etxName.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxSigninId.getText().toString())) {
            etxSigninId.setError(getString(R.string.error_required));
            etxSigninId.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxStreet.getText().toString())) {
            etxStreet.setError(getString(R.string.error_required));
            etxStreet.requestFocus();
            isValid = false;
        } else if (selectedSuburb == null) {
            etxSuburb.setError(getString(R.string.error_valid_suburb));
            etxSuburb.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxPostcode.getText().toString())) {
            etxPostcode.setError(getString(R.string.error_required));
            etxPostcode.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxPassword.getText().toString())) {
            etxPassword.setError(getString(R.string.error_required));
            etxPassword.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxConfirmPassword.getText().toString())) {
            etxConfirmPassword.setError(getString(R.string.error_required));
            etxConfirmPassword.requestFocus();
            isValid = false;
        } else if (!etxPassword.getText().toString().equals(etxConfirmPassword.getText().toString())) {
            etxConfirmPassword.setText("");
            etxConfirmPassword.setError(getString(R.string.error_confirm_password));
            etxConfirmPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void getSuburbs() {
        DataRequest getSuburbsDataRequest = new DataRequest(SignupActivity.this);
        getSuburbsDataRequest.execute(IWebService.GET_SUBURB, null, new DataRequest.CallBack() {
            public void onPreExecute() {
                rlvGlobalProgressbar.setVisibility(View.VISIBLE);
            }

            public void onPostExecute(String response) {
                try {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    if (!DataRequest.hasError(SignupActivity.this, response, true)) {
                        Gson gson = new Gson();
                        JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                        ArrayList<Suburb> tmpSuburbArrayList = gson.fromJson(
                                dataJObject.getJSONArray(IWebService.KEY_RES_SUBURB_LIST).toString(),
                                new TypeToken<ArrayList<Suburb>>() {
                                }.getType());
                        if (tmpSuburbArrayList != null) {
                            Log.e(TAG, "size = " + tmpSuburbArrayList.size());
                            suburbArrayList.addAll(tmpSuburbArrayList);
                            suburbArrayAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.txtSignin)
    void singIn() {
        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.txtGuest)
    void guestLogin() {
        showAlertForLocation();
    }


    // For custom alert dialog of location
    private void showAlertForLocation() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignupActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_guest_suburb, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        final AutoCompleteTextView atxSuburbDialog = (AutoCompleteTextView) dialogView.findViewById(R.id.atxSuburbDialog);
        atxSuburbDialog.setAdapter(suburbArrayAdapter);

        dialogView.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.txtGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atxSuburbDialog.setError(null);
                selectedSuburb = getSelectedSuburb(suburbArrayList, atxSuburbDialog.getText().toString());
                if (selectedSuburb == null) {
                    atxSuburbDialog.setError(getString(R.string.error_valid_suburb));
                    atxSuburbDialog.requestFocus();
                } else {
                    DBAdapter.insertUpdateMap(SignupActivity.this, IDatabase.IMap.SUBURB_ID,
                            selectedSuburb.suburb_id);
                    DBAdapter.insertUpdateMap(SignupActivity.this, IDatabase.IMap.SUBURB_NAME,
                            selectedSuburb.suburb_name);
                    DBAdapter.setMapKeyValueBoolean(SignupActivity.this, IDatabase.IMap.IS_LOGIN, false);

                    Intent intent = new Intent(SignupActivity.this, NavigationDrawerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }

}
