package com.shoppin.shoper.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.fragment.OrderDetailFragment;
import com.shoppin.shoper.fragment.OrderRequestFragment;
import com.shoppin.shoper.model.OrderRequest;
import com.shoppin.shoper.network.IWebService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.analytics.internal.zzy.C;
import static com.google.android.gms.analytics.internal.zzy.c;
import static com.google.android.gms.analytics.internal.zzy.r;

/**
 * Created by ubuntu on 8/8/16.
 */

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.MyViewHolder> {

    private static final String TAG = OrderRequestAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OrderRequest> orderRequestArrayList;
    private OrderRequestFragment orderRequestFragment;

    public OrderRequestAdapter(Context context, ArrayList<OrderRequest> orderRequestArrayList, OrderRequestFragment requestFragment) {
        this.mContext = context;
        this.orderRequestArrayList = orderRequestArrayList;
        this.orderRequestFragment = requestFragment;
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
        @BindView(R.id.txtAccepted)
        TextView txtAccepted;
        @BindView(R.id.txtReject)
        TextView txtReject;
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
                .inflate(R.layout.cell_order_request, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtOrderNumber.setText(orderRequestArrayList.get(position).order_number);
        holder.txtStreetName.setText(orderRequestArrayList.get(position).address1);
        holder.txtSuburb.setText(orderRequestArrayList.get(position).suburb_name);
        holder.txtTotalPrice.setText(orderRequestArrayList.get(position).total);
        holder.txtOrderDate.setText(orderRequestArrayList.get(position).delivery_date);
        holder.txtOrderTime.setText(orderRequestArrayList.get(position).delivery_time);

        holder.txtAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRequestFragment.SendOrderStatus(orderRequestArrayList.get(position).order_number, IWebService.KEY_REQ_STATUS_ACCPETED);

            }
        });
        holder.txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRequestFragment.SendOrderStatus(orderRequestArrayList.get(position).order_number,IWebService.KEY_REQ_STATUS_ORDER_REJECTED);

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDrawerActivity fca = (NavigationDrawerActivity) mContext;
                fca.switchContent(OrderDetailFragment
                        .newInstance(orderRequestArrayList.get(position).order_number));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderRequestArrayList.size();
    }

//    private class OnItemClickListener implements View.OnClickListener {
//
//
//        OnItemClickListener(SubCategory listModelSubCategory) {
//            tempValues1 = listModelSubCategory;
//        }
//
//        @Override
//        public void onClick(View arg0) {
//
//            Toast.makeText(
//
//                    mContext,
//                    "Test", Toast.LENGTH_LONG)
//                    .show();
//
//
//        }
//    }
}
