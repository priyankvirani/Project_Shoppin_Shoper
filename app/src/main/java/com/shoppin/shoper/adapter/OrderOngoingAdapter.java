package com.shoppin.shoper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppin.shoper.R;
import com.shoppin.shoper.model.OrderOngoing;

import java.util.ArrayList;

public class OrderOngoingAdapter extends BaseAdapter {

    private static final String TAG = OrderOngoingAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<OrderOngoing> orderOngoingsArrayList;
    private int fragmentNumber;


    public OrderOngoingAdapter(Context context, ArrayList<OrderOngoing> categoryArrayList, int frgNO) {
        this.context = context;
        this.orderOngoingsArrayList = categoryArrayList;
        this.fragmentNumber = frgNO;
    }

    @Override
    public int getCount() {
        return orderOngoingsArrayList == null ? 0 : orderOngoingsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderOngoingsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            if (fragmentNumber == 1) {
                convertView = View.inflate(context, R.layout.cell_order_ongoing, null);
            } else if (fragmentNumber == 2) {
                convertView = View.inflate(context, R.layout.cell_order_request, null);
            } else {
                convertView = View.inflate(context, R.layout.cell_order_history, null);
            }
            holder = new ViewHolder();
            holder.txtOrderNumber = (TextView) convertView.findViewById(R.id.txtOrderNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtOrderNumber.setText(orderOngoingsArrayList.get(position).getOrder_number());


        return convertView;

    }

    class ViewHolder {
        public TextView txtOrderNumber;
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
