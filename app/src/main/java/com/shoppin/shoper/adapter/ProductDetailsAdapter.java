package com.shoppin.shoper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.fragment.OrderDetailFragment;
import com.shoppin.shoper.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.analytics.internal.zzy.l;

/**
 * Created by ubuntu on 8/8/16.
 */

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder> {

    private static final String TAG = ProductDetailsAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private OrderDetailFragment detailFragment;

    public ProductDetailsAdapter(Context context, ArrayList<Product> productArrayList, OrderDetailFragment orderdetailsFragment) {
        this.mContext = context;
        this.productArrayList = productArrayList;
        this.detailFragment = orderdetailsFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtOrderNumber)
        TextView txtOrderNumber;
//        @BindView(R.id.txtStreetName)
//        TextView txtStreetName;
//        @BindView(R.id.txtSuburb)
//        TextView txtSuburb;
//        @BindView(R.id.txtTotalPrice)
//        TextView txtTotalPrice;
//        @BindView(R.id.txtOrderDate)
//        TextView txtOrderDate;
//        @BindView(R.id.txtOrderTime)
//        TextView txtOrderTime;
//        @BindView(R.id.txtAccepted)
//        TextView txtAccepted;
//        @BindView(R.id.txtReject)
//        TextView txtReject;
//        @BindView(R.id.card_view)
//        CardView cardView;


        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_product, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e(TAG, "Size " + productArrayList.size());

        holder.txtOrderNumber.setText(productArrayList.get(position).order_number);
//        holder.txtStreetName.setText(productArrayList.get(position).address1);
//        holder.txtSuburb.setText(productArrayList.get(position).suburb_name);
//        holder.txtTotalPrice.setText(productArrayList.get(position).total);
//        holder.txtOrderDate.setText(productArrayList.get(position).delivery_date);
//        holder.txtOrderTime.setText(productArrayList.get(position).delivery_time);
//
//        holder.txtAccepted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detailFragment.SendOrderStatus(productArrayList.get(position).order_number, IWebService.KEY_REQ_TRUE);
//
//            }
//        });
//        holder.txtReject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detailFragment.SendOrderStatus(productArrayList.get(position).order_number,IWebService.KEY_REQ_FALSE);
//
//            }
//        });
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavigationDrawerActivity fca = (NavigationDrawerActivity) mContext;
//                fca.switchContent(OrderDetailFragment
//                        .newInstance(productArrayList.get(position).order_number));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
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
