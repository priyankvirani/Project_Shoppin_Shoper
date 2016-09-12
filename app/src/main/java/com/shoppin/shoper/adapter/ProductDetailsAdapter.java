package com.shoppin.shoper.adapter;

import android.content.Context;
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

/**
 * Created by ubuntu on 8/8/16.
 */

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder> {

    private static final String TAG = ProductDetailsAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private OrderDetailFragment orderDetailFragment;
    private OnStatusChangeListener onStatusChangeListener;

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
        @BindView(R.id.txtProductQuantity)
        TextView txtProductQuantity;
        @BindView(R.id.imgItemStatus)
        ImageView imgItemStatus;
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
        holder.txtProductQuantity.setText(productArrayList.get(position).productQuntity);

        if (productArrayList.get(position).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE) {

            holder.imgItemStatus.setImageResource(R.drawable.not_available_red);

        } else if (productArrayList.get(position).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE) {

            holder.imgItemStatus.setImageResource(R.drawable.available_green);

        } else {

            holder.imgItemStatus.setImageResource(R.drawable.round);
        }

        holder.txtProductOption.setText(productArrayList.get(position).getSelectedOptions());

        Glide.with(mContext)
                .load(productArrayList.get(position).images[0])
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imgProduct);

        holder.imgItemStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (onStatusChangeListener != null) {

                    if (productArrayList.get(position).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE) {
                        productArrayList.get(position).productAvailability = IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE;

                        onStatusChangeListener.onStatusChange(position);


                    } else if (productArrayList.get(position).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE) {
                        productArrayList.get(position).productAvailability = IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE;
                        onStatusChangeListener.onStatusChange(position);

                    } else {
                        productArrayList.get(position).productAvailability = IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE;
                        onStatusChangeListener.onStatusChange(position);

                    }
                }

            }
        });


    }

    public void setOnStatusChangeListener(final OnStatusChangeListener onCartChangeListener) {
        this.onStatusChangeListener = onCartChangeListener;
    }

    public interface OnStatusChangeListener {
        public void onStatusChange(int position);
    }



    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

}
