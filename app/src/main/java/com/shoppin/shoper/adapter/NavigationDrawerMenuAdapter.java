package com.shoppin.shoper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppin.shoper.R;
import com.shoppin.shoper.model.NavigationDrawerMenuItem;





/**
 * To set Left side drawer
 *
 * @author chitrang
 */
public class NavigationDrawerMenuAdapter extends ArrayAdapter<NavigationDrawerMenuItem> {

    public NavigationDrawerMenuAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cell_navigation_drawer, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.rowIcon);
        icon.setImageResource(getItem(position).menuIcon);
        TextView title = (TextView) convertView.findViewById(R.id.rowTitle);
        title.setText(getItem(position).menuName);

        convertView.setTag(getItem(position).menuTagId);

        return convertView;
    }
}
