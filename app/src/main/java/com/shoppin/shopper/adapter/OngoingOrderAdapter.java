package com.shoppin.shopper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

    public OngoingOrderAdapter(Context context, ArrayList<OngoingOrder> orderRequestArrayList) {
        this.mContext = context;
        this.ongoingOrderArrayList = orderRequestArrayList;
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
        @BindView(R.id.llOrderStatus)
        LinearLayout llOrderStatus;
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
                .inflate(R.layout.cell_order_ongoing, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtOrderNumber.setText(ongoingOrderArrayList.get(position).order_number);
        holder.txtStreetName.setText(ongoingOrderArrayList.get(position).address1);
        holder.txtSuburb.setText(ongoingOrderArrayList.get(position).suburb_name);
        holder.txtTotalPrice.setText(mContext.getResources().getString(R.string.dollar_sign) + ongoingOrderArrayList.get(position).total);
        holder.txtOrderDate.setText(ongoingOrderArrayList.get(position).delivery_date);
        holder.txtOrderTime.setText(ongoingOrderArrayList.get(position).delivery_time);
        holder.txtCustomerName.setText(ongoingOrderArrayList.get(position).customerName);
        holder.txtItemCount.setText(ongoingOrderArrayList.get(position).itemCount);

        setOrderStatus(position,holder.txtOrderStatus,holder.llOrderStatus);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) mContext;
                if (navigationDrawerActivity != null) {
                    navigationDrawerActivity.switchContent(OrderDetailFragment
                            .newInstance(ongoingOrderArrayList.get(position).order_number, false));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return ongoingOrderArrayList.size();
    }

    public void setOrderStatus(int position, TextView txtorderStatus,LinearLayout llOrderStatus) {
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

}
