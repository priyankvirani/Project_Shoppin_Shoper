package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.adapter.OrderRequestAdapter;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.model.OrderRequest;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;

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

    @BindView(R.id.lvOrderRecyList)
    RecyclerView lvOrderRecyList;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    private OrderRequestAdapter orderRequestAdapter;
    private ArrayList<OrderRequest> orderRequestArrayList;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_order_request, container, false);
        ButterKnife.bind(this, layoutView);

        initView();

        return layoutView;
    }

    private void initView() {
        orderRequestArrayList = new ArrayList<OrderRequest>();
        LinearLayoutManager verticalLayoutManagaerdate
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        orderRequestAdapter = new OrderRequestAdapter(getActivity(),
                orderRequestArrayList, OrderRequestFragment.this);
        lvOrderRecyList.setLayoutManager(verticalLayoutManagaerdate);
        lvOrderRecyList.setAdapter(orderRequestAdapter);
        setOrderRequestData();

    }

    public void setOrderRequestData() {
        try {

            JSONObject loginParam = new JSONObject();
            loginParam.put(IWebService.KEY_REQ_ORDER_SUBURB_ID, DBAdapter.getEmployeSururbIDString(getActivity()));
            loginParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getEmployeIDString(getActivity()));


            DataRequest setdataRequest = new DataRequest(getActivity());
            setdataRequest.execute(IWebService.ORDER_REQUEST, loginParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    orderRequestArrayList.clear();
                    orderRequestAdapter.notifyDataSetChanged();

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            Gson gson = new Gson();

                            ArrayList<OrderRequest> tmpOrderRequestArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_ORDER_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<OrderRequest>>() {
                                    }.getType());

                            if (tmpOrderRequestArrayList != null) {
                                //Log.e(TAG, "Size :  " + orderRequestArrayList.size());
                                orderRequestArrayList.addAll(tmpOrderRequestArrayList);
                                orderRequestAdapter.notifyDataSetChanged();
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

    public void SendOrderStatus(String ordernumber,String status) {

        try {

            JSONObject orderstatusParam = new JSONObject();
            orderstatusParam.put(IWebService.KEY_REQ_ORDER_NUMBER, ordernumber);
            orderstatusParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getEmployeIDString(getActivity()));
            orderstatusParam.put(IWebService.KEY_RES_STATUS, status);


            DataRequest signinDataRequest = new DataRequest(getActivity());
            signinDataRequest.execute(IWebService.ACEEPT_ORDER, orderstatusParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            setOrderRequestData();

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
        if (getActivity() != null && getActivity() instanceof NavigationDrawerActivity) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle("Order Request");
        }
    }

}
