package com.shoppin.shoper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.activity.SigninActivity;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ubuntu on 15/8/16.
 */

public class MyAccountFragment extends BaseFragment {

    private static final String TAG = MyAccountFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, layoutView);

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

    @Override
    public void updateFragment() {
        super.updateFragment();
        NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getActivity();
        if (navigationDrawerActivity != null) {
            navigationDrawerActivity.setToolbarTitle(getActivity().getResources().getString(R.string.fragment_my_account));
        }
    }

}

