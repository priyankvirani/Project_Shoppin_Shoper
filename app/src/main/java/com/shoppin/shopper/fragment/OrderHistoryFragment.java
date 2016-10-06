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
import com.shoppin.shopper.adapter.OrderHistoryAdapter;
import com.shoppin.shopper.adapter.OngoingOrderAdapter;
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

import static com.shoppin.shopper.R.id.listViewProgressbar;

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

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<OrderHistory> orderHistoryArrayList;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private int pageNumber = 0;
    private boolean isPullRefresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this, layoutView);
        orderHistoryArrayList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerListOrderHistory.setLayoutManager(mLayoutManager);


        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(),
                orderHistoryArrayList, recyclerListOrderHistory);
        recyclerListOrderHistory.setAdapter(orderHistoryAdapter);
        getOrderHistoryData();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        IntentFilter intentFilter = new IntentFilter(IConstants.UPDATE_ORDER_HISTORY);
        // Here you can add additional actions which then would be received by the BroadcastReceiver

        broadcastManager.registerReceiver(receiver, intentFilter);

        orderHistoryAdapter.setOnLoadMoreListener(new OngoingOrderAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                orderHistoryArrayList.add(null);
                orderHistoryAdapter.notifyItemInserted(orderHistoryArrayList.size() - 1);

                //Load more data for reyclerview

                if (loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                    //loading = false;
                    pageNumber++;
                    //Do pagination.. i.e. fetch new data
                    getOrderHistoryData();

                }


            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = false;
                orderHistoryArrayList.clear();
                pageNumber = 0;
                getOrderHistoryData();

            }
        });
        swipeContainer.setColorSchemeResources(R.color.app_theme_1,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return layoutView;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null && action.equals(IConstants.UPDATE_ORDER_HISTORY)) {
                // perform your update
                orderHistoryArrayList.clear();
                pageNumber = 0;
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
                    if (orderHistoryArrayList == null) {
                        rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    }
                    loading = false;

                    if (pageNumber > 0) {
                        orderHistoryArrayList.remove(orderHistoryArrayList.size() - 1);
                        orderHistoryAdapter.notifyItemRemoved(orderHistoryArrayList.size());
                    }


                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

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
                                orderHistoryArrayList.addAll(tmpProductArrayList);

                                loading = true;
                                if (pageNumber == 0) {
                                    orderHistoryAdapter.notifyDataSetChanged();
                                    swipeContainer.setRefreshing(false);
                                }
                                if (isPullRefresh) {
                                    recyclerListOrderHistory.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                                }
                                orderHistoryAdapter.notifyDataSetChanged();
                                orderHistoryAdapter.setLoaded();

                                llEmptyList.setVisibility(View.GONE);
                            }

                        } else {

                            if (!Utils.isInternetAvailable(getActivity(), false)) {
                                Utils.showSnackbarAlert(getActivity(), IConstants.UPDATE_ORDER_HISTORY, getString(R.string.error_internet_check));
                                llEmptyList.setVisibility(View.VISIBLE);
                            } else if (orderHistoryArrayList == null) {
                                llEmptyList.setVisibility(View.VISIBLE);
                            }

                        }
                        Log.e(TAG, "Array list Size  : " + orderHistoryArrayList.size());

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
