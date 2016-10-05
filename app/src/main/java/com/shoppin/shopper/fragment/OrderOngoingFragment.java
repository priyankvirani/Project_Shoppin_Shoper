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
import com.shoppin.shopper.adapter.OngoingOrderAdapter;
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

    private OngoingOrderAdapter orderOngoingAdapter;
    private ArrayList<OngoingOrder> orderOngoingArrayList;


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
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_ongoing, container, false);
        ButterKnife.bind(this, layoutView);

        orderOngoingArrayList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerListOrderOngoing.setLayoutManager(mLayoutManager);
        orderOngoingAdapter = new OngoingOrderAdapter(getActivity(), orderOngoingArrayList, recyclerListOrderOngoing);
        recyclerListOrderOngoing.setAdapter(orderOngoingAdapter);
        getOngoingOrderData();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        IntentFilter intentFilter = new IntentFilter(IConstants.UPDATE_ORDER_ON_GOING);
        // Here you can add additional actions which then would be received by the BroadcastReceiver

        broadcastManager.registerReceiver(receiver, intentFilter);

        orderOngoingAdapter.setOnLoadMoreListener(new OngoingOrderAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom

                orderOngoingArrayList.add(null);
                orderOngoingAdapter.notifyItemInserted(orderOngoingArrayList.size() - 1);

                //Load more data for reyclerview

                if (loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                    //loading = false;
                    pageNumber++;
                    //Do pagination.. i.e. fetch new data
                    getOngoingOrderData();

                }


            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = false;
                orderOngoingArrayList.clear();
                pageNumber = 0;
                getOngoingOrderData();

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
            if (action != null && action.equals(IConstants.UPDATE_ORDER_ON_GOING)) {
                // perform your update
                Log.e(TAG, "IN Last Item Wow !");
                orderOngoingArrayList.clear();
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

                            if (orderOngoingArrayList == null) {
                                rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                            }
                            loading = false;
                            if (pageNumber > 0) {
                                orderOngoingArrayList.remove(orderOngoingArrayList.size() - 1);
                                orderOngoingAdapter.notifyItemRemoved(orderOngoingArrayList.size());
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

                                    ArrayList<OngoingOrder> tmpOrderRequestArrayList = gson.fromJson(
                                            dataJObject.getJSONArray(
                                                    IWebService.KEY_RES_ORDER_LIST)
                                                    .toString(),
                                            new TypeToken<ArrayList<OngoingOrder>>() {
                                            }.getType());


                                    if (tmpOrderRequestArrayList != null) {
                                        orderOngoingArrayList.addAll(tmpOrderRequestArrayList);

                                        loading = true;
                                        if (pageNumber == 0) {
                                            orderOngoingAdapter.notifyDataSetChanged();
                                            swipeContainer.setRefreshing(false);

                                        }
                                        if (isPullRefresh) {
                                            recyclerListOrderOngoing.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                                        }
                                        orderOngoingAdapter.notifyItemInserted(orderOngoingArrayList.size());
                                        orderOngoingAdapter.setLoaded();
                                        llEmptyList.setVisibility(View.GONE);

                                    }

                                } else {

                                    loading = false;
                                    listViewProgressbar.setVisibility(View.GONE);

                                    if (!Utils.isInternetAvailable(getActivity(), false)) {
                                        Utils.showSnackbarAlert(getActivity(), IConstants.UPDATE_ORDER_ON_GOING, getString(R.string.error_internet_check));
                                        llEmptyList.setVisibility(View.VISIBLE);
                                    } else if (orderOngoingArrayList == null) {
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
            //getOngoingOrderData();
        }
    }
}
