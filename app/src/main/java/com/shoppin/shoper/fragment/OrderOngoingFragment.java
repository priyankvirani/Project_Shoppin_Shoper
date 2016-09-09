package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.activity.SplashScreenActivity;
import com.shoppin.shoper.adapter.OngoingOrderAdapter;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.model.OngoingOrder;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderOngoingFragment extends BaseFragment {

    private static final String TAG = OrderOngoingFragment.class.getSimpleName();
    @BindView(R.id.recyclerListOrderRequest)
    RecyclerView lvOrderRecyList;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    private OngoingOrderAdapter orderOngoingAdapter;
    private ArrayList<OngoingOrder> orderOngoingArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_ongoing, container, false);
        ButterKnife.bind(this, layoutView);

        initView();


        return layoutView;
    }

    private void initView() {
        orderOngoingArrayList = new ArrayList<OngoingOrder>();
        LinearLayoutManager verticalLayoutManagaerdate
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        orderOngoingAdapter = new OngoingOrderAdapter(getActivity(), orderOngoingArrayList);
        lvOrderRecyList.setLayoutManager(verticalLayoutManagaerdate);

        lvOrderRecyList.setAdapter(orderOngoingAdapter);
        getOngoingOrderData();

    }

    public void getOngoingOrderData() {
        try {

            JSONObject ongoingOrderParam = new JSONObject();
            ongoingOrderParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));


            DataRequest setdataRequest = new DataRequest(getActivity());
            setdataRequest.execute(IWebService.ONGOING_ORDER, ongoingOrderParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    orderOngoingArrayList.clear();
                    orderOngoingAdapter.notifyDataSetChanged();

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            Gson gson = new Gson();

                            ArrayList<OngoingOrder> tmpOrderRequestArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_ORDER_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<OngoingOrder>>() {
                                    }.getType());

                            if (tmpOrderRequestArrayList != null) {
                                //Log.e(TAG, "Size :  " + orderRequestArrayList.size());
                                orderOngoingArrayList.addAll(tmpOrderRequestArrayList);
                                orderOngoingAdapter.notifyDataSetChanged();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void updateFragment() {
        super.updateFragment();
        NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getActivity();
        if (navigationDrawerActivity != null) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle(getActivity().getResources().getString(R.string.fragment_order_ongoing));
        }
    }
}
