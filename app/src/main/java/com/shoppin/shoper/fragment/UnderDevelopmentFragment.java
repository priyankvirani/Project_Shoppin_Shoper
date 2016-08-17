package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.activity.SignupActivity;

import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class UnderDevelopmentFragment extends BaseFragment {

    private static final String TAG = SignupActivity.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_under_development, container, false);
        ButterKnife.bind(this, layoutView);


        return layoutView;
    }

    @Override
    public void updateFragment() {
        super.updateFragment();
        if (getActivity() != null && getActivity() instanceof NavigationDrawerActivity) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle("Under Development");
        }
    }
}
