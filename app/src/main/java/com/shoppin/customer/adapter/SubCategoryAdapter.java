package com.shoppin.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class SubCategoryAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private String TAG = SubCategoryAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<SubCategory> dataListModelSubCategoriesArrayList;
    private SubCategory listModelSubCategoryTempValues = null;

    public SubCategoryAdapter(Context context, ArrayList<SubCategory> dataListModelSubCategories) {
        mContext = context;
        dataListModelSubCategoriesArrayList = dataListModelSubCategories;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (dataListModelSubCategoriesArrayList.size() <= 0)
            return 1;
        return dataListModelSubCategoriesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.cell_subcategory_home, null);
            holder = new ViewHolder();
            holder.txtsubCategory = (TextView) vi
                    .findViewById(R.id.txtSubcategory);
            holder.imgsubcategory = (ImageView) vi.findViewById(R.id.imgSubcategory);
            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();

        if (dataListModelSubCategoriesArrayList.size() <= 0) {
            holder.txtsubCategory.setText("No Data");
        } else {
            listModelSubCategoryTempValues = null;

            listModelSubCategoryTempValues = dataListModelSubCategoriesArrayList.get(position);
            holder.txtsubCategory.setText(listModelSubCategoryTempValues.subcat_name);
            Glide.with(mContext).load(listModelSubCategoryTempValues.subcat_image).into(holder.imgsubcategory);
        }
        holder.imgsubcategory.setOnClickListener(new OnItemClickListener(listModelSubCategoryTempValues));

        return vi;
    }

    public static class ViewHolder {
        public TextView txtsubCategory;
        public ImageView imgsubcategory;


    }

    private class OnItemClickListener implements View.OnClickListener {
        private SubCategory listModelSubCategoryArr;

        OnItemClickListener(SubCategory listModelSubCategory) {
            listModelSubCategoryArr = listModelSubCategory;
        }

        @Override
        public void onClick(View arg0) {
            Toast.makeText(mContext, listModelSubCategoryArr.subcat_name, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
