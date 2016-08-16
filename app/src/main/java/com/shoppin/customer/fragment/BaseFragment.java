package com.shoppin.customer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppin.customer.activity.NavigationDrawerActivity;

/**
 * Created by ubuntu on 13/8/16.
 */

public class BaseFragment extends Fragment implements IUpdateFragment {
    public View layoutView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void updateFragment() {
        if (getActivity() != null && getActivity() instanceof NavigationDrawerActivity) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle("");
        }
    }
}

