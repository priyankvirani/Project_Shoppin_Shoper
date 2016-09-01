package com.shoppin.shoper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.model.OrderOngoing;
import com.shoppin.shoper.model.OrderRequest;

import java.util.ArrayList;

public class OrderRequestAdapter extends BaseAdapter {

    private static final String TAG = OrderRequestAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<OrderRequest> orderRequestArrayList;


    public OrderRequestAdapter(Context context, ArrayList<OrderRequest> orderRequestArrayList) {
        this.context = context;
        this.orderRequestArrayList = orderRequestArrayList;

    }

    @Override
    public int getCount() {
        return orderRequestArrayList == null ? 0 : orderRequestArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderRequestArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cell_order_request, null);

            holder = new ViewHolder();
            holder.txtOrderNumber = (TextView) convertView.findViewById(R.id.txtOrderNumber);
            holder.txtStreetName = (TextView) convertView.findViewById(R.id.txtStreetName);
            holder.txtSuburb = (TextView) convertView.findViewById(R.id.txtSuburb);
            holder.txtTotalPrice = (TextView) convertView.findViewById(R.id.txtTotalPrice);
            holder.txtOrderDate = (TextView) convertView.findViewById(R.id.txtOrderDate);
            holder.txtOrderTime = (TextView) convertView.findViewById(R.id.txtOrderTime);
            holder.txtAccepted = (TextView) convertView.findViewById(R.id.txtAccepted);
            holder.txtReject = (TextView) convertView.findViewById(R.id.txtReject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtOrderNumber.setText(orderRequestArrayList.get(position).order_number);
        holder.txtStreetName.setText(orderRequestArrayList.get(position).address1);
        holder.txtSuburb.setText(orderRequestArrayList.get(position).suburb_name);
        holder.txtTotalPrice.setText(orderRequestArrayList.get(position).total);
        holder.txtOrderDate.setText(orderRequestArrayList.get(position).delivery_date);
        holder.txtOrderTime.setText(orderRequestArrayList.get(position).delivery_time);


        return convertView;

    }

    class ViewHolder {
        public TextView txtOrderNumber;
        public TextView txtStreetName;
        public TextView txtSuburb;
        public TextView txtTotalPrice;
        public TextView txtOrderDate;
        public TextView txtOrderTime;
        public TextView txtAccepted;
        public TextView txtReject;

    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

        }


    }


}
