package com.shoppin.shopper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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

public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = OrderHistoryAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OrderHistory> orderHistoryArrayList;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;




    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderRequestArrayList) {
        this.mContext = context;
        this.orderHistoryArrayList = orderRequestArrayList;


    }

    @Override
    public int getItemViewType(int position) {
        return orderHistoryArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_order_history, parent, false);

            return new MainViewHolder(v);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            return new ProgressViewHolder(v);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MainViewHolder) {
            MainViewHolder mainViewHolder = (MainViewHolder) holder;
            mainViewHolder.txtOrderNumber.setText(orderHistoryArrayList.get(position).order_number);
            mainViewHolder.txtStreetName.setText(orderHistoryArrayList.get(position).address1);
            mainViewHolder.txtSuburb.setText(orderHistoryArrayList.get(position).suburb_name);
            mainViewHolder.txtTotalPrice.setText(mContext.getResources().getString(R.string.dollar_sign) + orderHistoryArrayList.get(position).total);
            mainViewHolder.txtOrderDate.setText(orderHistoryArrayList.get(position).delivery_date);
            mainViewHolder.txtOrderTime.setText(orderHistoryArrayList.get(position).delivery_time);
            mainViewHolder.txtCustomerName.setText(orderHistoryArrayList.get(position).customerName);
            mainViewHolder.txtItemCount.setText(orderHistoryArrayList.get(position).itemCount);

            if (orderHistoryArrayList.get(position).status == IWebService.KEY_REQ_STATUS_CANCELLED) {
                mainViewHolder.txtStatusDate.setVisibility(View.INVISIBLE);
                mainViewHolder.txtStatusTime.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.cancel), null);
                mainViewHolder.txtStatusTime.setText(mContext.getString(R.string.order_status_cancelled));
                mainViewHolder.txtStatusTime.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                mainViewHolder.txtStatusDate.setVisibility(View.VISIBLE);
                mainViewHolder.txtStatusTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mainViewHolder.txtStatusTime.setTextColor(mContext.getResources().getColor(R.color.app_theme_1));
                mainViewHolder.txtStatusDate.setText(orderHistoryArrayList.get(position).shipping_date);
                mainViewHolder.txtStatusTime.setText(orderHistoryArrayList.get(position).shipping_time);

            }


            mainViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) mContext;
                    if (navigationDrawerActivity != null) {
                        navigationDrawerActivity.switchContent(OrderDetailFragment
                                .newInstance(orderHistoryArrayList.get(position).order_number, true, IConstants.UPDATE_ORDER_HISTORY));
                    }
                }
            });
        } else if(holder instanceof ProgressViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList == null ? 0 : orderHistoryArrayList.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
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

        public MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {


        public ProgressViewHolder(View view) {
            super(view);

        }
    }


}
