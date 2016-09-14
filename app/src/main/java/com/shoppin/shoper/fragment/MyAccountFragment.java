package com.shoppin.shoper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.activity.SigninActivity;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.model.Suburb;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;
import com.shoppin.shoper.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ubuntu on 15/8/16.
 */

public class MyAccountFragment extends BaseFragment {

    private static final String TAG = MyAccountFragment.class.getSimpleName();
    @BindView(R.id.etxName)
    EditText etxName;

    @BindView(R.id.etxMobileNumber)
    EditText etxMobileNumber;

    @BindView(R.id.etxEmail)
    EditText etxEmail;

    @BindView(R.id.atxSuburb)
    AutoCompleteTextView atxSuburb;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.llProfileDetqails)
    LinearLayout llProfileDetqails;

    private ArrayList<Suburb> suburbArrayList;
    private ArrayAdapter<Suburb> suburbArrayAdapter;
    private Suburb selectedSuburb;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, layoutView);

        suburbArrayList = new ArrayList<>();
        suburbArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, suburbArrayList);
        atxSuburb.setAdapter(suburbArrayAdapter);

        getEmployeeProfileData();

        return layoutView;
    }

    @OnClick(R.id.txtSingOut)
    void logOut() {

        DBAdapter.insertUpdateMap(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID, "");
        DBAdapter.insertUpdateMap(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID, "");

        Intent intent = new Intent(getActivity(), SigninActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    public void getEmployeeProfileData() {
        try {

            JSONObject employeeprofileParam = new JSONObject();
            employeeprofileParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));

            DataRequest getEmployeeProfileDataRequest = new DataRequest(getActivity());
            getEmployeeProfileDataRequest.execute(IWebService.GET_EMPLOYEE_PROFILE, employeeprofileParam.toString(), new DataRequest.CallBack() {
                        public void onPreExecute() {
                            rlvGlobalProgressbar.setVisibility(View.VISIBLE);

                        }

                        public void onPostExecute(String response) {
                            rlvGlobalProgressbar.setVisibility(View.GONE);
                            try {

                                if (!DataRequest.hasError(getActivity(), response, true)) {

                                    JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                                    etxName.setText(dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_NAME));
                                    etxMobileNumber.setText(dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_MOBILE));
                                    etxEmail.setText(dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_EMAIL));
                                    DBAdapter.insertUpdateMap(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID, dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_SUBURB_ID));


                                    Gson gson = new Gson();

                                    ArrayList<Suburb> tmpSuburbArrayList = gson.fromJson(
                                            dataJObject.getJSONArray(IWebService.KET_RES_SUBURB_LIST).toString(),
                                            new TypeToken<ArrayList<Suburb>>() {
                                            }.getType());
                                    if (tmpSuburbArrayList != null) {
                                        Log.e(TAG, "size = " + tmpSuburbArrayList.size());
                                        suburbArrayList.addAll(tmpSuburbArrayList);
                                        suburbArrayAdapter.notifyDataSetChanged();
                                    }

                                    atxSuburb.setText(dataJObject.getString(IWebService.KEY_RES_SUBURB_NAME));


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

            );

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.txtUpdate)
    void updateProfile() {
        if (updateValidation()) {
            updateEmployeeProfileData();
        }
    }


    private void updateEmployeeProfileData() {
        try {

            JSONObject updateEmployeeProfileDataParam = new JSONObject();
            updateEmployeeProfileDataParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            updateEmployeeProfileDataParam.put(IWebService.KEY_REQ_EMPLOYEE_NAME, etxName.getText().toString());
            updateEmployeeProfileDataParam.put(IWebService.KEY_REQ_EMPLOYEE_MOBILE, etxMobileNumber.getText().toString());
            updateEmployeeProfileDataParam.put(IWebService.KEY_REQ_EMPLOYEE_EMAIL, etxEmail.getText().toString());
            updateEmployeeProfileDataParam.put(IWebService.KEY_REQ_EMPLOYEE_SUBURB_ID, selectedSuburb.suburb_id);

            DataRequest updateEmployeeProfileDataRequest = new DataRequest(getActivity());
            updateEmployeeProfileDataRequest.execute(IWebService.UPDATE_EMPLOYEE_PROFILE, updateEmployeeProfileDataParam.toString(), new DataRequest.CallBack() {
                        public void onPreExecute() {
                            rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                            llProfileDetqails.setVisibility(View.GONE);
                        }

                        public void onPostExecute(String response) {
                            rlvGlobalProgressbar.setVisibility(View.GONE);
                            llProfileDetqails.setVisibility(View.VISIBLE);
                            try {

                                if (!DataRequest.hasError(getActivity(), response, true)) {

                                    JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                                    //Gson gson = new Gson();

                                    DBAdapter.insertUpdateMap(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID, dataJObject.getString(IWebService.KEY_RES_EMPLOYEE_SUBURB_ID));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

            );

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean updateValidation() {
        boolean isValid = true;
        etxName.setError(null);
        etxMobileNumber.setError(null);
        etxEmail.setError(null);
        atxSuburb.setError(null);

        selectedSuburb = Utils.getSelectedSuburb(suburbArrayList, atxSuburb.getText().toString());

        if (Utils.isNullOrEmpty(etxName.getText().toString())) {
            etxName.setError(getString(R.string.error_required));
            etxName.requestFocus();
            isValid = false;
        } else if (Utils.isNullOrEmpty(etxMobileNumber.getText().toString())) {
            etxMobileNumber.setError(getString(R.string.error_required));
            etxMobileNumber.requestFocus();
            isValid = false;
        } else if (Utils.isValidEmail(etxEmail.getText().toString())) {
            etxEmail.setError(getString(R.string.error_required_email));
            etxEmail.requestFocus();
            isValid = false;
        } else if (selectedSuburb == null) {
            atxSuburb.setError(getString(R.string.error_valid_suburb));
            atxSuburb.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void updateFragment() {
        super.updateFragment();
        NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getActivity();
        if (navigationDrawerActivity != null) {
            navigationDrawerActivity.setToolbarTitle(getActivity().getResources().getString(R.string.fragment_my_account));
        }
    }

}

