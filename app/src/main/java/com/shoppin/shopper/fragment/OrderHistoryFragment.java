package com.shoppin.shopper.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.adapter.OrderHistoryAdapter;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.model.OrderHistory;
import com.shoppin.shopper.network.DataRequest;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.IConstants;
import com.shoppin.shopper.utils.Utils;

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

    @BindView(R.id.rlvContent)
    RelativeLayout rlvContent;

    @BindView(R.id.recyclerListOrderHistory)
    RecyclerView recyclerListOrderHistory;

    @BindView(R.id.llEmptyList)
    LinearLayout llEmptyList;

    @BindView(R.id.txtEmptyList)
    TextView txtEmptyList;

    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<OrderHistory> orderOngoingArrayList;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private int pageNumber = 0;


    @BindView(R.id.listViewProgressbar)
    CrystalPreloader listViewProgressbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this, layoutView);
        orderOngoingArrayList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(),
                orderOngoingArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerListOrderHistory.setLayoutManager(mLayoutManager);
        recyclerListOrderHistory.setAdapter(orderHistoryAdapter);
        getOrderHistoryData();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        IntentFilter intentFilter = new IntentFilter(IConstants.UPDATE_ORDER_HISTORY);
        // Here you can add additional actions which then would be received by the BroadcastReceiver

        broadcastManager.registerReceiver(receiver, intentFilter);

        recyclerListOrderHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

//                    Log.e(TAG, "ARREY SIZE        :" + orderOngoingArrayList.size());
//                    Log.e(TAG, "visibleItemCount  :" + visibleItemCount);
//                    Log.e(TAG, "totalItemCount    :" + orderOngoingArrayList.size());
//                    Log.e(TAG, "pastVisiblesItems :" + orderOngoingArrayList.size());


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            pageNumber++;
                            Log.e(TAG, "IN Last Item Wow !");
                            listViewProgressbar.setVisibility(View.VISIBLE);
                            //Do pagination.. i.e. fetch new data
                            getOrderHistoryData();
                        }
                    }
                }
            }
        });


        return layoutView;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null && action.equals(IConstants.UPDATE_ORDER_HISTORY)) {
                // perform your update
                getOrderHistoryData();
            }

        }
    };

    public void getOrderHistoryData() {
        try {

            JSONObject orderHistoryParam = new JSONObject();
            orderHistoryParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            orderHistoryParam.put(IWebService.KEY_REQ_PAGE_NO, pageNumber);
            DataRequest setdataRequest = new DataRequest(getActivity());
            setdataRequest.execute(IWebService.ORDER_HISTORY, orderHistoryParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    //orderOngoingArrayList.clear();
                    orderHistoryAdapter.notifyDataSetChanged();

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    listViewProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, false)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            if (dataJObject == null) {
                                loading = false;

                            }

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
                                llEmptyList.setVisibility(View.GONE);
                            }

                        } else {

                            if (!Utils.isInternetAvailable(getActivity(), false)) {
                                Utils.showSnackbarAlert(getActivity(), IConstants.UPDATE_ORDER_HISTORY, getString(R.string.error_internet_check));
                                llEmptyList.setVisibility(View.VISIBLE);
                            } else if (orderOngoingArrayList == null) {
                                llEmptyList.setVisibility(View.VISIBLE);
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
