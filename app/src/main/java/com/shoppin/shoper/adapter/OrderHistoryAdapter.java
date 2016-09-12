package com.shoppin.shoper.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.fragment.OrderDetailFragment;
import com.shoppin.shoper.fragment.OrderHistoryFragment;
import com.shoppin.shoper.model.OrderHistory;
import com.shoppin.shoper.network.IWebService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.analytics.internal.zzy.o;

/**
 * Created by ubuntu on 8/8/16.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private static final String TAG = OrderHistoryAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OrderHistory> orderHistoryArrayList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderRequestArrayList) {
        this.mContext = context;
        this.orderHistoryArrayList = orderRequestArrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtOrderNumber)
        TextView txtOrderNumber;
        @BindView(R.id.txtStreetName)
        TextView txtStreetName;
        @BindView(R.id.txtSuburb)
        TextView txtSuburb;
        @BindView(R.id.txtTotalPrice)
        TextView txtTotalPrice;
        @BindView(R.id.txtOrderDate)
        TextView txtOrderDate;
        @BindView(R.id.txtOrderTime)
        TextView txtOrderTime;
        @BindView(R.id.txtStatusDate)
        TextView txtStatusDate;
        @BindView(R.id.txtStatusTime)
        TextView txtStatusTime;

        @BindView(R.id.card_view)
        CardView cardView;


        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_order_history, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtOrderNumber.setText(orderHistoryArrayList.get(position).order_number);
        holder.txtStreetName.setText(orderHistoryArrayList.get(position).address1);
        holder.txtSuburb.setText(orderHistoryArrayList.get(position).suburb_name);
        holder.txtTotalPrice.setText(mContext.getResources().getString(R.string.dollar_sign)+orderHistoryArrayList.get(position).total);
        holder.txtOrderDate.setText(orderHistoryArrayList.get(position).delivery_date);
        holder.txtOrderTime.setText(orderHistoryArrayList.get(position).delivery_time);
        holder.txtStatusDate.setText(orderHistoryArrayList.get(position).shipping_date);
        holder.txtStatusTime.setText(orderHistoryArrayList.get(position).shipping_time);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) mContext;
                if(navigationDrawerActivity!=null) {
                    navigationDrawerActivity.switchContent(OrderDetailFragment
                            .newInstance(orderHistoryArrayList.get(position).order_number,true));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }


}
