package com.shoppin.shoper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.adapter.UnderDevlopmentAdapter;
import com.shoppin.shoper.model.OrderOngoing;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderOngoingFragment extends BaseFragment {

    private static final String TAG = OrderOngoingFragment.class.getSimpleName();
    private ListView lvOrderList;
    private UnderDevlopmentAdapter orderOngoingAdapter;
    private ArrayList<OrderOngoing> orderOngoingArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_ongoing, container, false);
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
        orderOngoingArrayList = new ArrayList<OrderOngoing>();

        lvOrderList = (ListView) layoutView.findViewById(R.id.lvOrderList);

        orderOngoingAdapter = new UnderDevlopmentAdapter(getActivity(),
                orderOngoingArrayList, 1);
        lvOrderList.setAdapter(orderOngoingAdapter);
        setData();

    }

    private void setData() {
        for (int i = 0; i < 10; i++) {
            OrderOngoing orderOngoingSched = new OrderOngoing();
            orderOngoingSched.setOrder_number("Order Number : " + (i + 1));
            orderOngoingArrayList.add(orderOngoingSched);
        }


    }

    @Override
    public void updateFragment() {
        super.updateFragment();
        if (getActivity() != null && getActivity() instanceof NavigationDrawerActivity) {
            ((NavigationDrawerActivity) getActivity()).setToolbarTitle("Order Ongoing");
        }
    }
}
