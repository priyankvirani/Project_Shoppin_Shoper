package com.shoppin.shopper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.fragment.OrderDetailFragment;
import com.shoppin.shopper.model.OrderHistory;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.IConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


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
        @BindView(R.id.txtCustomerName)
        TextView txtCustomerName;
        @BindView(R.id.txtItemCount)
        TextView txtItemCount;


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
        holder.txtTotalPrice.setText(mContext.getResources().getString(R.string.dollar_sign) + orderHistoryArrayList.get(position).total);
        holder.txtOrderDate.setText(orderHistoryArrayList.get(position).delivery_date);
        holder.txtOrderTime.setText(orderHistoryArrayList.get(position).delivery_time);
        holder.txtCustomerName.setText(orderHistoryArrayList.get(position).customerName);
        holder.txtItemCount.setText(orderHistoryArrayList.get(position).itemCount);

        if(orderHistoryArrayList.get(position).status == IWebService.KEY_REQ_STATUS_CANCELLED){
            holder.txtStatusDate.setVisibility(View.INVISIBLE);
            holder.txtStatusTime.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.cancel), null);
            holder.txtStatusTime.setText(mContext.getString(R.string.order_status_cancelled));
            holder.txtStatusTime.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            holder.txtStatusDate.setVisibility(View.VISIBLE);
            holder.txtStatusTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.txtStatusTime.setTextColor(mContext.getResources().getColor(R.color.app_theme_1));
            holder.txtStatusDate.setText(orderHistoryArrayList.get(position).shipping_date);
            holder.txtStatusTime.setText(orderHistoryArrayList.get(position).shipping_time);

        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) mContext;
                if (navigationDrawerActivity != null) {
                    navigationDrawerActivity.switchContent(OrderDetailFragment
                            .newInstance(orderHistoryArrayList.get(position).order_number, true, IConstants.UPDATE_ORDER_HISTORY));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }


}
