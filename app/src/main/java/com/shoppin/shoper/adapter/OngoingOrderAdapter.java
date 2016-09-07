package com.shoppin.shoper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.fragment.OrderDetailFragment;
import com.shoppin.shoper.fragment.OrderOngoingFragment;
import com.shoppin.shoper.model.OngoingOrder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 8/8/16.
 */

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.MyViewHolder> {

    private static final String TAG = OngoingOrderAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OngoingOrder> ongoingOrderArrayList;
    private OrderOngoingFragment orderOngoingFragment;

    public OngoingOrderAdapter(Context context, ArrayList<OngoingOrder> orderRequestArrayList, OrderOngoingFragment ongoingFragment) {
        this.mContext = context;
        this.ongoingOrderArrayList = orderRequestArrayList;
        this.orderOngoingFragment = ongoingFragment;
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
        @BindView(R.id.txtOrderStatus)
        TextView txtOrderStatus;

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
                .inflate(R.layout.cell_order_ongoing, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtOrderNumber.setText(ongoingOrderArrayList.get(position).order_number);
        holder.txtStreetName.setText(ongoingOrderArrayList.get(position).address1);
        holder.txtSuburb.setText(ongoingOrderArrayList.get(position).suburb_name);
        holder.txtTotalPrice.setText(ongoingOrderArrayList.get(position).total);
        holder.txtOrderDate.setText(ongoingOrderArrayList.get(position).delivery_date);
        holder.txtOrderTime.setText(ongoingOrderArrayList.get(position).delivery_time);

        orderOngoingFragment.checkOrderStatus(mContext,Integer.valueOf(ongoingOrderArrayList.get(position).status), holder.txtOrderStatus);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDrawerActivity fca = (NavigationDrawerActivity) mContext;
                fca.switchContent(OrderDetailFragment
                        .newInstance(ongoingOrderArrayList.get(position).order_number));
            }
        });
    }



    @Override
    public int getItemCount() {
        return ongoingOrderArrayList.size();
    }
}
