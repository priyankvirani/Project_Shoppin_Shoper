package com.shoppin.shopper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.fragment.OrderDetailFragment;
import com.shoppin.shopper.model.OngoingOrder;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.IConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 4/10/16.
 */

public class OrderOngoingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = OrderOngoingAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OngoingOrder> ongoingOrderArrayList;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    int demo = 0;


    public OrderOngoingAdapter(Context context, ArrayList<OngoingOrder> orderRequestArrayList) {
        this.mContext = context;
        this.ongoingOrderArrayList = orderRequestArrayList;


    }

    @Override
    public int getItemViewType(int position) {
        return ongoingOrderArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOrderStatus(int position, TextView txtorderStatus, LinearLayout llOrderStatus) {
        if (ongoingOrderArrayList.get(position).status == IWebService.KEY_REQ_STATUS_ACCEPTED) {

            llOrderStatus.setBackgroundResource(R.drawable.button_background_green);
            txtorderStatus.setText(ongoingOrderArrayList.get(position).statusLable);
            txtorderStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.accepted), null);

        } else if (ongoingOrderArrayList.get(position).status == IWebService.KEY_REQ_STATUS_PUECHASING) {

            llOrderStatus.setBackgroundResource(R.drawable.button_background_blue);
            txtorderStatus.setText(ongoingOrderArrayList.get(position).statusLable);
            txtorderStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.purchasing), null);

        } else if (ongoingOrderArrayList.get(position).status == IWebService.KEY_REQ_STATUS_SHIPPING) {

            llOrderStatus.setBackgroundResource(R.drawable.button_background_yellow);
            txtorderStatus.setText(ongoingOrderArrayList.get(position).statusLable);
            txtorderStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.shipping), null);

        } else if (ongoingOrderArrayList.get(position).status == IWebService.KEY_REQ_STATUS_COMPLETED) {

            llOrderStatus.setBackgroundResource(R.drawable.button_background_green);
            txtorderStatus.setText(ongoingOrderArrayList.get(position).statusLable);
            txtorderStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.completed_white), null);

        } else if (ongoingOrderArrayList.get(position).status == IWebService.KEY_REQ_STATUS_ON_HOLD) {

            llOrderStatus.setBackgroundResource(R.drawable.button_background_red);
            txtorderStatus.setText(ongoingOrderArrayList.get(position).statusLable);
            txtorderStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.hold), null);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_order_ongoing, parent, false);

            return new MainViewHolder(v);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            demo++;
            Log.e(TAG, "demo " + demo);

            return new ProgressViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MainViewHolder) {

            MainViewHolder mainViewHolder = (MainViewHolder) holder;

            mainViewHolder.txtOrderNumber.setText(ongoingOrderArrayList.get(position).order_number);
            mainViewHolder.txtStreetName.setText(ongoingOrderArrayList.get(position).address1);
            mainViewHolder.txtSuburb.setText(ongoingOrderArrayList.get(position).suburb_name);
            mainViewHolder.txtTotalPrice.setText(mContext.getResources().getString(R.string.dollar_sign) + ongoingOrderArrayList.get(position).total);
            mainViewHolder.txtOrderDate.setText(ongoingOrderArrayList.get(position).delivery_date);
            mainViewHolder.txtOrderTime.setText(ongoingOrderArrayList.get(position).delivery_time);
            mainViewHolder.txtCustomerName.setText(ongoingOrderArrayList.get(position).customerName);
            mainViewHolder.txtItemCount.setText(ongoingOrderArrayList.get(position).itemCount);

            setOrderStatus(position, mainViewHolder.txtOrderStatus, mainViewHolder.llOrderStatus);

            mainViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) mContext;
                    if (navigationDrawerActivity != null) {
                        navigationDrawerActivity.switchContent(OrderDetailFragment
                                .newInstance(ongoingOrderArrayList.get(position).order_number, false, IConstants.UPDATE_ORDER_ON_GOING));
                    }
                }
            });

        } else if (holder instanceof ProgressViewHolder) {


        }
    }

    @Override
    public int getItemCount() {
        return ongoingOrderArrayList == null ? 0 : ongoingOrderArrayList.size();
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
        @BindView(R.id.txtOrderStatus)
        TextView txtOrderStatus;
        @BindView(R.id.llOrderStatus)
        LinearLayout llOrderStatus;
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
