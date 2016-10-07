package com.shoppin.shopper.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.adapter.OrderOngoingAdapter;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.model.OngoingOrder;
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

public class OrderOngoingFragment extends BaseFragment {

    private static final String TAG = OrderOngoingFragment.class.getSimpleName();
    @BindView(R.id.recyclerListOrderOngoing)
    RecyclerView recyclerListOrderOngoing;

    @BindView(R.id.rlvContent)
    RelativeLayout rlvContent;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.llEmptyList)
    LinearLayout llEmptyList;

    @BindView(R.id.txtEmptyList)
    TextView txtEmptyList;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private OrderOngoingAdapter orderOngoingAdapter;
    private ArrayList<OngoingOrder> orderOngoingArrayList;


    private LinearLayoutManager mLayoutManager;
    private int pageNumber = 0;

    protected Handler handler;

    private boolean isLoading;
    private boolean canLoadMore = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean isTempArraySize = true;
    private int tempArraySize;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_ongoing, container, false);
        ButterKnife.bind(this, layoutView);

        handler = new Handler();
        orderOngoingArrayList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerListOrderOngoing.setLayoutManager(mLayoutManager);
        orderOngoingAdapter = new OrderOngoingAdapter(getActivity(), orderOngoingArrayList);
        recyclerListOrderOngoing.setAdapter(orderOngoingAdapter);

        getOngoingOrderData();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter(IConstants.UPDATE_ORDER_ON_GOING);
        broadcastManager.registerReceiver(receiver, intentFilter);


        recyclerListOrderOngoing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "onScrolled");
                if (dy > 0) //check for scroll down
                {
                    Log.d(TAG, "dy = " + dy);
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    Log.d(TAG, "isLoading = " + isLoading);
                    Log.d(TAG, "canLoadMore = " + canLoadMore);
                    if (!isLoading && canLoadMore) {
                        Log.d(TAG, "visibleItemCount = " + visibleItemCount);
                        Log.d(TAG, "pastVisibleItems = " + pastVisibleItems);
                        Log.d(TAG, "(visibleItemCount + pastVisibleItems) = " + (visibleItemCount + pastVisibleItems));
                        Log.d(TAG, "totalItemCount = " + totalItemCount);
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;
                            pageNumber++;
                            getOngoingOrderData();
                        }
                    }
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                canLoadMore = true;
                swipeContainer.setRefreshing(true);
                orderOngoingArrayList.clear();
                orderOngoingAdapter.notifyDataSetChanged();
                pageNumber = 0;
                getOngoingOrderData();


            }
        });
        swipeContainer.setColorSchemeResources(R.color.app_theme_1);


        return layoutView;
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        super.onDestroyView();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null && action.equals(IConstants.UPDATE_ORDER_ON_GOING)) {
                orderOngoingArrayList.clear();
                orderOngoingAdapter.notifyDataSetChanged();
                pageNumber = 0;
                getOngoingOrderData();
            }

        }
    };


    public void getOngoingOrderData() {
        try {

            final JSONObject ongoingOrderParam = new JSONObject();
            ongoingOrderParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            ongoingOrderParam.put(IWebService.KEY_REQ_PAGE_NO, pageNumber);

            DataRequest setDataRequest = new DataRequest(getActivity());
            setDataRequest.execute(IWebService.ONGOING_ORDER, ongoingOrderParam.toString(), new DataRequest.CallBack() {
                        public void onPreExecute() {

                            if (orderOngoingArrayList.isEmpty()) {
                                rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                            }

                            if (pageNumber > 0) {
                                orderOngoingArrayList.add(null);
                                orderOngoingAdapter.notifyItemInserted(orderOngoingArrayList.size() - 1);
                            }

                        }

                        public void onPostExecute(String response) {
                            rlvGlobalProgressbar.setVisibility(View.GONE);

                            try {
                                canLoadMore = false;
                                isLoading = false;
                                swipeContainer.setRefreshing(false);
                                if (pageNumber > 0) {
                                    orderOngoingArrayList.remove(orderOngoingArrayList.size() - 1);
                                    orderOngoingAdapter.notifyItemRemoved(orderOngoingArrayList.size());
                                }


                                if (!DataRequest.hasError(getActivity(), response, false)) {

                                    JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                                    Gson gson = new Gson();

                                    final ArrayList<OngoingOrder> tmpOrderOngoingArrayList = gson.fromJson(
                                            dataJObject.getJSONArray(
                                                    IWebService.KEY_RES_ORDER_LIST)
                                                    .toString(),
                                            new TypeToken<ArrayList<OngoingOrder>>() {
                                            }.getType());

                                    if (isTempArraySize) {
                                        tempArraySize = tmpOrderOngoingArrayList.size();
                                        isTempArraySize = false;
                                    }

                                    if (tmpOrderOngoingArrayList.size() >= tempArraySize) {
                                        canLoadMore = true;
                                    }

                                    if (tmpOrderOngoingArrayList != null) {
                                        orderOngoingArrayList.addAll(tmpOrderOngoingArrayList);
                                        orderOngoingAdapter.notifyDataSetChanged();
                                        swipeContainer.setRefreshing(false);
                                        llEmptyList.setVisibility(View.GONE);
                                    } else {
                                        llEmptyList.setVisibility(View.VISIBLE);
                                    }

                                } else {

                                    if (!Utils.isInternetAvailable(getActivity(), false)) {
                                        Utils.showSnackbarAlert(getActivity(), IConstants.UPDATE_ORDER_ON_GOING, getString(R.string.error_internet_check));

                                    } else if (orderOngoingArrayList.isEmpty()) {
                                        llEmptyList.setVisibility(View.VISIBLE);
                                    }

                                }

                                Log.e(TAG, "Array list Size  : " + orderOngoingArrayList.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

            );

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }


    }


    @Override
    public void updateFragment() {
        super.updateFragment();
        NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getActivity();
        if (navigationDrawerActivity != null) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle(getActivity().getResources().getString(R.string.fragment_order_ongoing));
            //getOngoingOrderData();
        }
    }
}
