package com.shoppin.customer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppin.customer.R;
import com.shoppin.customer.model.Category;
import com.shoppin.customer.model.SubCategory;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<SubCategory> subCategoryArrayList;
    private SubCategoryHorizontalAdapter subCategoryHorizontalAdapter;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @Override
    public int getCount() {
        return categoryArrayList == null ? 0 : categoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cell_category_home_fragment, null);
            holder = new ViewHolder();
            holder.txtCategory = (TextView) convertView.findViewById(R.id.txtCategory);
            holder.imgViewAllCategory = (ImageView) convertView.findViewById(R.id.imgViewAllCategory);
            holder.rclHorizontalSubCategoty = (RecyclerView) convertView.findViewById(R.id.recycler_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.e(TAG, "" + categoryArrayList.get(position).cat_id);

        holder.txtCategory.setText(categoryArrayList.get(position).cat_name);

        subCategoryArrayList = categoryArrayList.get(position).subCategoryArrayList;
        subCategoryHorizontalAdapter = new SubCategoryHorizontalAdapter(context, subCategoryArrayList, categoryArrayList.get(position).cat_id);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rclHorizontalSubCategoty.setLayoutManager(horizontalLayoutManager);
        holder.rclHorizontalSubCategoty.setAdapter(subCategoryHorizontalAdapter);

        if (categoryArrayList.get(position).isCategoryExpand()) {
            holder.rclHorizontalSubCategoty.setVisibility(View.VISIBLE);

        } else {
            holder.rclHorizontalSubCategoty.setVisibility(View.GONE);
        }
        //Log.e(TAG, "" + listModelCategoryTempValues.getCat_name() + "," + listModelCategoryTempValues.isCategoryExpand() + "," + position);

        holder.txtCategory.setOnClickListener(new OnItemClickListener(position));
        holder.imgViewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "Size : " + categoryArrayList.size());

//                NavigationDrawerActivity fca = (NavigationDrawerActivity) context;
//
//                //Intent intent = new Intent(context, SubCategoryFragment.class);
//                Bundle args = new Bundle();
//                args.putSerializable(SubCategoryFragment.DATA_ITEMCATEGORY_EXTRA, (Serializable) categoryArrayList);
//                args.putInt(SubCategoryFragment.EXTRA_SELECTED_CELL_ID, position);
//                SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
//                subCategoryFragment.setArguments(args);
//                fca.switchContent(subCategoryFragment, true);

                //intent.putExtra(SubCategoryFragment.BUNDLEVISIBLE, args);
                //context.startActivity(intent);

            }
        });
        return convertView;

    }

    class ViewHolder {
        public TextView txtCategory;
        public RecyclerView rclHorizontalSubCategoty;
        //        public TextView txtViewAll;
        public ImageView imgViewAllCategory;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            Category listModelCategory = categoryArrayList.get(mPosition);

            if (listModelCategory.isCategoryExpand()) {
                listModelCategory.setCategoryExpand(false);
            } else {
                listModelCategory.setCategoryExpand(true);
            }
            notifyDataSetChanged();
            Toast.makeText(context, "Category : " + listModelCategory.cat_name, Toast.LENGTH_LONG).show();
        }


    }


}
