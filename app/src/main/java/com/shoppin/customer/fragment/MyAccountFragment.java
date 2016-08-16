package com.shoppin.customer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppin.customer.R;
import com.shoppin.customer.activity.SigninActivity;
import com.shoppin.customer.activity.SignupActivity;
import com.shoppin.customer.database.DBAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shoppin.customer.database.IDatabase.IMap;

/**
 * Created by ubuntu on 15/8/16.
 */

public class MyAccountFragment extends BaseFragment {

    private static final String TAG = SignupActivity.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, layoutView);

        return layoutView;
    }

    @OnClick(R.id.btnLogOut)
    void logOut() {
        DBAdapter.insertUpdateMap(getActivity(), IMap.SUBURB_ID, "");
        DBAdapter.insertUpdateMap(getActivity(), IMap.SUBURB_NAME, "");

        Intent intent = new Intent(getActivity(), SigninActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
