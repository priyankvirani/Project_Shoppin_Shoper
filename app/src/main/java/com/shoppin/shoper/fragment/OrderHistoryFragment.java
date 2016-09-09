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
import com.shoppin.shoper.adapter.OrderHistoryAdapter;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.model.OrderHistory;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderHistoryFragment extends BaseFragment {

    private static final String TAG = OrderHistoryFragment.class.getSimpleName();
    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.recyclerListOrderRequest)
    RecyclerView lvOrderRecyList;

    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<OrderHistory> orderOngoingArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this, layoutView);
        initView();


        return layoutView;
    }

    private void initView() {
        orderOngoingArrayList = new ArrayList<OrderHistory>();
        LinearLayoutManager verticalLayoutManagaerdate
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(),
                orderOngoingArrayList);
        lvOrderRecyList.setLayoutManager(verticalLayoutManagaerdate);
        lvOrderRecyList.setAdapter(orderHistoryAdapter);
        getOrderHistoryData();
    }

    public void getOrderHistoryData() {
        try {

            JSONObject orderHistoryParam = new JSONObject();
            orderHistoryParam.put(IWebService.KEY_REQ_EMPLOYEE_ID,DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));

            DataRequest setdataRequest = new DataRequest(getActivity());
            setdataRequest.execute(IWebService.ORDER_HISTORY, orderHistoryParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    orderOngoingArrayList.clear();
                    orderHistoryAdapter.notifyDataSetChanged();

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            Gson gson = new Gson();


                            ArrayList<OrderHistory> tmpProductArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_ORDER_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<OrderHistory>>() {
                                    }.getType());

                            if (tmpProductArrayList != null) {
                                //Log.e(TAG, "Size :  " + productArrayList.size());
                                orderOngoingArrayList.addAll(tmpProductArrayList);
                                orderHistoryAdapter.notifyDataSetChanged();
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
            navigationDrawerActivity.setToolbarTitle(getActivity().getResources().getString(R.string.fragment_order_history));

        }
    }
}
