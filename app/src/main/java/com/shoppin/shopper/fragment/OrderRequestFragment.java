package com.shoppin.shopper.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.adapter.OrderRequestAdapter;
import com.shoppin.shopper.adapter.OngoingOrderAdapter;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.model.OrderRequest;
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

public class OrderRequestFragment extends BaseFragment {

    private static final String TAG = OrderRequestFragment.class.getSimpleName();
    public static final String ORDER_NUMBER = "order_number";

    @BindView(R.id.recyclerListOrderRequest)
    RecyclerView recyclerListOrderRequest;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.rlvContent)
    RelativeLayout rlvContent;

    @BindView(R.id.llEmptyList)
    LinearLayout llEmptyList;

    @BindView(R.id.txtEmptyList)
    TextView txtEmptyList;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;


    private OrderRequestAdapter orderRequestAdapter;
    private ArrayList<OrderRequest> orderRequestArrayList;


    @BindView(R.id.listViewProgressbar)
    CrystalPreloader listViewProgressbar;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private int pageNumber = 0;
    private boolean isPullRefresh = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_order_request, container, false);
        ButterKnife.bind(this, layoutView);

        orderRequestArrayList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerListOrderRequest.setLayoutManager(mLayoutManager);

        orderRequestAdapter = new OrderRequestAdapter(getActivity(), orderRequestArrayList, recyclerListOrderRequest);
        recyclerListOrderRequest.setAdapter(orderRequestAdapter);
        orderRequestAdapter.setOnStatusChangeListener(new OrderRequestAdapter.OnStatusChangeListener() {
            @Override
            public void onStatusChange(int position, boolean status) {
                if (status) {
                    sendOrderStatus(position, IWebService.KEY_REQ_STATUS_ACCEPTED);
                } else {
                    sendOrderStatus(position, IWebService.KEY_REQ_STATUS_REJECTED);
                }
            }
        });


        getOrderRequestData();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        IntentFilter intentFilter = new IntentFilter(IConstants.UPDATE_ORDER_REQUEST);
        // Here you can add additional actions which then would be received by the BroadcastReceiver

        broadcastManager.registerReceiver(receiver, intentFilter);

        orderRequestAdapter.setOnLoadMoreListener(new OngoingOrderAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                orderRequestArrayList.add(null);
                orderRequestAdapter.notifyItemInserted(orderRequestArrayList.size() - 1);

                //Load more data for reyclerview

                if (loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                    //loading = false;
                    pageNumber++;
                    //Do pagination.. i.e. fetch new data
                    getOrderRequestData();

                }


            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = false;
                orderRequestArrayList.clear();
                pageNumber = 0;
                getOrderRequestData();

            }
        });
        swipeContainer.setColorSchemeResources(R.color.app_theme_1,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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
            if (action != null && action.equals(IConstants.UPDATE_ORDER_REQUEST)) {
                // perform your update
                orderRequestArrayList.clear();
                pageNumber = 0;
                getOrderRequestData();
            }

        }
    };

    public void getOrderRequestData() {
        // orderRequestArrayList.clear();
        try {

            JSONObject orderRequestParam = new JSONObject();
            orderRequestParam.put(IWebService.KEY_REQ_ORDER_SUBURB_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_SUBURB_ID));
            orderRequestParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            orderRequestParam.put(IWebService.KEY_REQ_PAGE_NO, pageNumber);

            DataRequest getOrderDataRequest = new DataRequest(getActivity());
            getOrderDataRequest.execute(IWebService.ORDER_REQUEST, orderRequestParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    if (orderRequestArrayList == null) {
                        rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    }
                    loading = false;

                    if (pageNumber > 0) {
                        orderRequestArrayList.remove(orderRequestArrayList.size() - 1);
                        orderRequestAdapter.notifyItemRemoved(orderRequestArrayList.size());
                    }


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

                            ArrayList<OrderRequest> tmpOrderRequestArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_ORDER_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<OrderRequest>>() {
                                    }.getType());

                            if (tmpOrderRequestArrayList != null) {
                                orderRequestArrayList.addAll(tmpOrderRequestArrayList);


                                loading = true;
                                if (pageNumber == 0) {
                                    orderRequestAdapter.notifyDataSetChanged();
                                    swipeContainer.setRefreshing(false);
                                }
                                if (isPullRefresh) {
                                    recyclerListOrderRequest.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                                }
                                orderRequestAdapter.notifyDataSetChanged();
                                orderRequestAdapter.setLoaded();
                                llEmptyList.setVisibility(View.GONE);
                            }

                        } else {
                            if (!Utils.isInternetAvailable(getActivity(), false)) {
                                Utils.showSnackbarAlert(getActivity(), IConstants.UPDATE_ORDER_REQUEST, getString(R.string.error_internet_check));
                                llEmptyList.setVisibility(View.VISIBLE);
                            } else if (orderRequestArrayList == null) {
                                llEmptyList.setVisibility(View.VISIBLE);
                            }

                        }
                        Log.e(TAG, "Array list Size  : " + orderRequestArrayList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendOrderStatus(final int position, int status) {

        try {

            JSONObject orderStatusParam = new JSONObject();
            orderStatusParam.put(IWebService.KEY_REQ_ORDER_NUMBER, orderRequestArrayList.get(position).order_number);
            orderStatusParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            orderStatusParam.put(IWebService.KEY_RES_STATUS, status);
            orderStatusParam.put(IWebService.KEY_REQ_PRODUCT_COMMENTS, IWebService.KEY_REQ_NULL);

            DataRequest signInDataRequest = new DataRequest(getActivity());
            signInDataRequest.execute(IWebService.ORDER_ACTION, orderStatusParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {
                            NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getActivity();
                            if (navigationDrawerActivity != null) {
                                navigationDrawerActivity.switchContent(OrderDetailFragment
                                        .newInstance(orderRequestArrayList.get(position).order_number, false, IConstants.UPDATE_ORDER_REQUEST));
                            }
                            getOrderRequestData();


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
            navigationDrawerActivity.setToolbarTitle(getActivity().getResources().getString(R.string.fragment_order_request));
            //getOrderRequestData();
        }

    }


}
