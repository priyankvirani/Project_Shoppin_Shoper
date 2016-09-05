package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.adapter.ProductDetailsAdapter;
import com.shoppin.shoper.model.OrderRequest;
import com.shoppin.shoper.model.Product;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderDetailFragment extends BaseFragment {

    private static final String TAG = OrderDetailFragment.class.getSimpleName();
    public static final String ORDER_NUMBER = "order_number";

    public static OrderDetailFragment newInstance(String order_number) {
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NUMBER, order_number);
        orderDetailFragment.setArguments(bundle);
        return orderDetailFragment;
    }


    private ProductDetailsAdapter productDetailsAdapter;
    private ArrayList<Product> productArrayList;
    private String order_number;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.lvOrderRecyList)
    RecyclerView lvOrderRecyList;

    @BindView(R.id.txtOrderNumber)
    TextView txtOrderNumber;

    @BindView(R.id.txtStreetName)
    TextView txtStreetName;

    @BindView(R.id.txtSuburb)
    TextView txtSuburb;

    @BindView(R.id.txtTown)
    TextView txtTown;

    @BindView(R.id.txtOrderDate)
    TextView txtOrderDate;

    @BindView(R.id.txtOrderTime)
    TextView txtOrderTime;

    @BindView(R.id.txtphoneNumber)
    TextView txtphoneNumber;

    @BindView(R.id.txtOrderPrice)
    TextView txtOrderPrice;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, layoutView);

        initView();


        if (getArguments() != null) {
            order_number = getArguments().getString(ORDER_NUMBER);

            Log.e(TAG, "Order Number :  -  " + order_number);

        }

        getOrderDetailsData();

        return layoutView;
    }

    private void initView() {
        productArrayList = new ArrayList<Product>();
        LinearLayoutManager verticalLayoutManagaerdate
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        productDetailsAdapter = new ProductDetailsAdapter(getActivity(),
                productArrayList, OrderDetailFragment.this);
        lvOrderRecyList.setLayoutManager(verticalLayoutManagaerdate);
        lvOrderRecyList.setAdapter(productDetailsAdapter);


    }

//    private void setData() {
//        for (int i = 0; i < 10; i++) {
//            Product productSched = new Product();
//            productSched.setOrder_number("Product Name : " + (i + 1));
//            productArrayList.add(productSched);
//        }
//    }

    public void getOrderDetailsData() {
        try {

            JSONObject loginParam = new JSONObject();
            loginParam.put(IWebService.KEY_REQ_ORDER_NUMBER, order_number);


            DataRequest setdataRequest = new DataRequest(getActivity());
            setdataRequest.execute(IWebService.ORDER_DETAILS, loginParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                    productArrayList.clear();
                    productDetailsAdapter.notifyDataSetChanged();

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);
                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            Gson gson = new Gson();

                            txtOrderNumber.setText(dataJObject.getString(IWebService.KEY_REQ_ORDER_NUMBER));
                            txtStreetName.setText(dataJObject.getString(IWebService.KEY_RES_ADDRESS1));
                            txtSuburb.setText(dataJObject.getString(IWebService.KEY_RES_SUBURB_NAME));
                            txtphoneNumber.setText(dataJObject.getString(IWebService.KEY_RES_CUSTOMER_MOBILE));
                            txtOrderDate.setText(dataJObject.getString(IWebService.KEY_RES_DELIVERY_DATR));
                            txtOrderTime.setText(dataJObject.getString(IWebService.KEY_RES_DELIVERY_TIME));
                            txtOrderPrice.setText(dataJObject.getString(IWebService.KEY_RES_TOTAL));


                            ArrayList<Product> tmpProductArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_PRODUCT_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<OrderRequest>>() {
                                    }.getType());

                            if (tmpProductArrayList != null) {
                                Log.e(TAG, "Size :  " + productArrayList.size());
                                productArrayList.addAll(tmpProductArrayList);
                                productDetailsAdapter.notifyDataSetChanged();
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
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle("Order Details");
        }
    }
}
