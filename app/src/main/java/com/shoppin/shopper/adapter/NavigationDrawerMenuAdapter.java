package com.shoppin.shopper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppin.shopper.R;
import com.shoppin.shopper.model.NavigationDrawerMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * To set Left side drawer
 *
 * @author priyank
 */
public class NavigationDrawerMenuAdapter extends RecyclerView.Adapter<NavigationDrawerMenuAdapter.MyViewHolder>  {

    private ArrayList<NavigationDrawerMenu> navigationDrawerMenuArrayList;
    private OnItemClickListener itemClickListener;

    public NavigationDrawerMenuAdapter(ArrayList<NavigationDrawerMenu> navigationDrawerMenuArrayList) {
        this.navigationDrawerMenuArrayList = navigationDrawerMenuArrayList;
    }

    @Override
    public int getItemCount() {
        return navigationDrawerMenuArrayList == null ? 0 : navigationDrawerMenuArrayList.size();
    }

    @Override
    public NavigationDrawerMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_navigation_drawer, parent, false);
        return new NavigationDrawerMenuAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NavigationDrawerMenuAdapter.MyViewHolder holder, final int position) {
        holder.rowIcon.setImageResource(navigationDrawerMenuArrayList.get(position).menuIcon);
        holder.rowTitle.setText(navigationDrawerMenuArrayList.get(position).menuName);
        holder.cellRoot.setTag(navigationDrawerMenuArrayList.get(position).menuTagId);

        holder.cellRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cellRoot)
        View cellRoot;

        @BindView(R.id.rowTitle)
        TextView rowTitle;

        @BindView(R.id.rowIcon)
        ImageView rowIcon;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}