package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.adapter.OrderOngoingAdapter;
import com.shoppin.shoper.model.OrderOngoing;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderDetailFragment extends BaseFragment {

    private static final String TAG = OrderDetailFragment.class.getSimpleName();

    public static OrderDetailFragment newInstance(String demo) {
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        return orderDetailFragment;
    }

    private ListView lvOrderList;
    private OrderOngoingAdapter orderOngoingAdapter;
    private ArrayList<OrderOngoing> orderOngoingArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_details, container, false);
        initView();
        return layoutView;
    }

    private void initView() {
        orderOngoingArrayList = new ArrayList<OrderOngoing>();

        lvOrderList = (ListView) layoutView.findViewById(R.id.lvOrderList);

        orderOngoingAdapter = new OrderOngoingAdapter(getActivity(),
                orderOngoingArrayList, 4);
        lvOrderList.setAdapter(orderOngoingAdapter);
        setData();

    }

    private void setData() {
        for (int i = 0; i < 10; i++) {
            OrderOngoing orderOngoingSched = new OrderOngoing();
            orderOngoingSched.setOrder_number("Product Name : " + (i + 1));
            orderOngoingArrayList.add(orderOngoingSched);
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
