package com.shoppin.shoper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.shoppin.shoper.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectionAdapter<T> extends RecyclerView.Adapter<SelectionAdapter.MyViewHolder> {

    private static final String TAG = SelectionAdapter.class.getSimpleName();

    private ArrayList<T> filterArrayList;
    private IBindAdapterValues<T> bindAdapterValues;

    public SelectionAdapter(ArrayList<T> filterArrayList) {
        this.filterArrayList = filterArrayList;
    }

    @Override
    public int getItemCount() {
        return filterArrayList == null ? 0 : filterArrayList.size();
    }

    @Override
    public SelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_selection, parent, false);
        return new SelectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectionAdapter.MyViewHolder holder, final int position) {
        bindAdapterValues.bindValues(holder, position);
    }

    public void setBindAdapterInterface(IBindAdapterValues<T> bindAdapterValues) {
        this.bindAdapterValues = bindAdapterValues;
    }

    public interface IBindAdapterValues<T> {
        // public void bindValues(Holder holder, T object);
        public void bindValues(SelectionAdapter.MyViewHolder holder, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtSelectionValue)
        public CheckBox txtSelectionValue;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
