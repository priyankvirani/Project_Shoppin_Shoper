package com.shoppin.shopper.adapter;

import android.content.Context;
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
import com.shoppin.shopper.model.OrderRequest;
import com.shoppin.shopper.utils.IConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 8/8/16.
 */

public class OrderRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = OrderRequestAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OrderRequest> orderRequestArrayList;
    private OnStatusChangeListener onStatusChangeListener;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    public OrderRequestAdapter(Context context, ArrayList<OrderRequest> orderRequestArrayList) {
        this.mContext = context;
        this.orderRequestArrayList = orderRequestArrayList;


    }

    @Override
    public int getItemViewType(int position) {
        return orderRequestArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_order_request, parent, false);

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
            mainViewHolder.txtOrderNumber.setText(orderRequestArrayList.get(position).order_number);
            mainViewHolder.txtStreetName.setText(orderRequestArrayList.get(position).address1);
            mainViewHolder.txtSuburb.setText(orderRequestArrayList.get(position).suburb_name);
            mainViewHolder.txtTotalPrice.setText(mContext.getResources().getString(R.string.dollar_sign) + orderRequestArrayList.get(position).total);
            mainViewHolder.txtOrderDate.setText(orderRequestArrayList.get(position).delivery_date);
            mainViewHolder.txtOrderTime.setText(orderRequestArrayList.get(position).delivery_time);
            mainViewHolder.txtItemCount.setText(orderRequestArrayList.get(position).itemCount);


            mainViewHolder.txtAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStatusChangeListener.onStatusChange(position, true);

                }
            });
            mainViewHolder.txtReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStatusChangeListener.onStatusChange(position, false);

                }
            });

            mainViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) mContext;
                    if (navigationDrawerActivity != null) {
                        navigationDrawerActivity.switchContent(OrderDetailFragment
                                .newInstance(orderRequestArrayList.get(position).order_number, false, IConstants.UPDATE_ORDER_REQUEST));
                    }
                }
            });
        } else if (holder instanceof ProgressViewHolder) {

        }
    }

    public void setOnStatusChangeListener(final OnStatusChangeListener onCartChangeListener) {
        this.onStatusChangeListener = onCartChangeListener;
    }

    public interface OnStatusChangeListener {
        public void onStatusChange(int position, boolean status);
    }


    @Override
    public int getItemCount() {
        return orderRequestArrayList == null ? 0 : orderRequestArrayList.size();
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
        @BindView(R.id.txtAccepted)
        TextView txtAccepted;
        @BindView(R.id.txtReject)
        TextView txtReject;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.txtItemCount)
        TextView txtItemCount;

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
