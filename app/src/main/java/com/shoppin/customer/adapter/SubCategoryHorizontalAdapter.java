package com.shoppin.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shoppin.customer.R;
import com.shoppin.customer.model.SubCategory;

import java.util.ArrayList;

/**
 * Created by ubuntu on 8/8/16.
 */

public class SubCategoryHorizontalAdapter extends RecyclerView.Adapter<SubCategoryHorizontalAdapter.MyViewHolder> {
    private static final String TAG = SubCategoryHorizontalAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<SubCategory> subCategoryArrayList;
    private String cat_id;

    public SubCategoryHorizontalAdapter(Context context, ArrayList<SubCategory> subCategoryArrayList, String cat_id) {
        this.context = context;
        this.subCategoryArrayList = subCategoryArrayList;
        this.cat_id = cat_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_subcategory_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtsubCategory.setText(subCategoryArrayList.get(position).subcat_name);
        //Log.e(TAG, " : " + listModelSubCategoryTempValues.getSubcat_name());
        //Log.e(TAG, " : " + listModelSubCategoryTempValues.getItemCellCategoryName());
        Glide.with(context).load(subCategoryArrayList.get(position).subcat_image).into(holder.imgSubcategory);
        holder.imgSubcategory.setOnClickListener(new OnItemClickListener(subCategoryArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return subCategoryArrayList == null ? 0 : subCategoryArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtsubCategory;
        ImageView imgSubcategory;

        MyViewHolder(View vi) {
            super(vi);
            txtsubCategory = (TextView) vi
                    .findViewById(R.id.txtSubcategory);
            imgSubcategory = (ImageView) vi.findViewById(R.id.imgSubcategory);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private SubCategory subCategory;

        OnItemClickListener(SubCategory subCategory) {
            this.subCategory = subCategory;
        }

        @Override
        public void onClick(View arg0) {
            Toast.makeText(context, cat_id + "," + subCategory.subcat_name, Toast.LENGTH_LONG).show();
            //Log.e(TAG, " :    " + subCategory.getItemCellCategoryName() + "," + subCategory.getSubcat_name());
        }
    }
}
