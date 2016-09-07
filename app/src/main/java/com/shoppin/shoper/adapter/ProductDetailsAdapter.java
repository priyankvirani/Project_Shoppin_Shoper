package com.shoppin.shoper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shoppin.shoper.R;
import com.shoppin.shoper.fragment.OrderDetailFragment;
import com.shoppin.shoper.model.Product;
import com.shoppin.shoper.network.IWebService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.order;

/**
 * Created by ubuntu on 8/8/16.
 */

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder> {

    private static final String TAG = ProductDetailsAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private OrderDetailFragment orderDetailFragment;

    public ProductDetailsAdapter(Context context, ArrayList<Product> productArrayList, OrderDetailFragment orderdetailsFragment) {
        this.mContext = context;
        this.productArrayList = productArrayList;
        this.orderDetailFragment = orderdetailsFragment;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtProductName)
        TextView txtProductName;
        @BindView(R.id.imgProduct)
        ImageView imgProduct;
        @BindView(R.id.txtProductPrice)
        TextView txtProductPrice;
        @BindView(R.id.txtProductUnit)
        TextView txtProductUnit;
        @BindView(R.id.txtItemStatus)
        TextView txtItemStatus;
        @BindView(R.id.txtProductOption)
        TextView txtProductOption;


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

        holder.txtProductName.setText(productArrayList.get(position).productName);
        holder.txtProductPrice.setText(productArrayList.get(position).saleprice1);
        holder.txtProductUnit.setText(productArrayList.get(position).saleprice1);


        if (Integer.valueOf(productArrayList.get(position).productAvailability) == -1) {
            holder.txtItemStatus.setText(mContext.getResources().getString(R.string.not_available));
            holder.txtItemStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.reject), null);
        } else if (Integer.valueOf(productArrayList.get(position).productAvailability) == 1) {
            holder.txtItemStatus.setText(mContext.getResources().getString(R.string.available));
            holder.txtItemStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.accepted), null);
        } else {
            holder.txtItemStatus.setText(mContext.getResources().getString(R.string.normal));
        }

        for (int i = 0; i < productArrayList.get(position).productOptionArrayList.size(); i++) {
            holder.txtProductOption.setText(productArrayList.get(position).getSelectedOptions());

        }

        Glide.with(mContext)
                .load(productArrayList.get(position).images[0])
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imgProduct);

        holder.txtItemStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.txtItemStatus.setText(orderDetailFragment.submitProductSetValue(mContext,
                        holder.txtItemStatus.getText().toString(), productArrayList.get(position).productItemId));




            }
        });


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

}
