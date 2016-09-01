package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.adapter.OrderRequestAdapter;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.model.OrderOngoing;
import com.shoppin.shoper.model.OrderRequest;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderRequestFragment extends BaseFragment {

    private static final String TAG = OrderRequestFragment.class.getSimpleName();
    private ListView lvOrderList;
    private OrderRequestAdapter orderRequestAdapter;
    private ArrayList<OrderRequest> orderRequestArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_request, container, false);
        ButterKnife.bind(this, layoutView);

        initView();
        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavigationDrawerActivity fca = (NavigationDrawerActivity) getActivity();
                fca.switchContent(OrderDetailFragment
                        .newInstance("demo"));
            }
        });
        return layoutView;
    }

    private void initView() {
        orderRequestArrayList = new ArrayList<OrderRequest>();

        lvOrderList = (ListView) layoutView.findViewById(R.id.lvOrderList);

        orderRequestAdapter = new OrderRequestAdapter(getActivity(),
                orderRequestArrayList);
        lvOrderList.setAdapter(orderRequestAdapter);
        setData();

    }

    private void setData() {
//        for (int i = 0; i < 10; i++) {
//            OrderOngoing orderOngoingSched = new OrderOngoing();
//            orderOngoingSched.setOrder_number("Order Number : " + (i + 1));
//            orderRequestArrayList.add(orderOngoingSched);
//        }

        try {

            JSONObject loginParam = new JSONObject();
            loginParam.put(IWebService.KEY_REQ_ORDER_SUBURB_ID, DBAdapter.getEmployeSururbIDString(getActivity()));


            DataRequest signinDataRequest = new DataRequest(getActivity());
            signinDataRequest.execute(IWebService.ORDER_REQUEST, loginParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {

                }

                public void onPostExecute(String response) {
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

    @Override
    public void updateFragment() {
        super.updateFragment();
        if (getActivity() != null && getActivity() instanceof NavigationDrawerActivity) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle("Order Request");
        }
    }
}
