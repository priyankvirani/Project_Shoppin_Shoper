package com.shoppin.shopper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 6/9/16.
 */

public class OrderHistory implements Parcelable {

    @SerializedName("order_number")
    public String order_number;
    @SerializedName("order_slot_id")
    public String order_slot_id;
    @SerializedName("shipping_address_id")
    public String shipping_address_id;
    @SerializedName("total")
    public String total;
    @SerializedName("status")
    public int status;
    @SerializedName("address1")
    public String address1;
    @SerializedName("suburb_name")
    public String suburb_name;
    @SerializedName("delivery_date")
    public String delivery_date;
    @SerializedName("delivery_time")
    public String delivery_time;
    @SerializedName("shipping_date")
    public String shipping_date;
    @SerializedName("shipping_time")
    public String shipping_time;
    @SerializedName("customer_name")
    public String customerName;
    @SerializedName("item_count")
    public String itemCount;

    public OrderHistory() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_number);
        dest.writeString(this.order_slot_id);
        dest.writeString(this.shipping_address_id);
        dest.writeString(this.total);
        dest.writeInt(this.status);
        dest.writeString(this.address1);
        dest.writeString(this.suburb_name);
        dest.writeString(this.delivery_date);
        dest.writeString(this.delivery_time);
        dest.writeString(this.shipping_date);
        dest.writeString(this.shipping_time);
        dest.writeString(this.customerName);
        dest.writeString(this.itemCount);
    }

    protected OrderHistory(Parcel in) {
        this.order_number = in.readString();
        this.order_slot_id = in.readString();
        this.shipping_address_id = in.readString();
        this.total = in.readString();
        this.status = in.readInt();
        this.address1 = in.readString();
        this.suburb_name = in.readString();
        this.delivery_date = in.readString();
        this.delivery_time = in.readString();
        this.shipping_date = in.readString();
        this.shipping_time = in.readString();
        this.customerName = in.readString();
        this.itemCount = in.readString();
    }

    public static final Creator<OrderHistory> CREATOR = new Creator<OrderHistory>() {
        @Override
        public OrderHistory createFromParcel(Parcel source) {
            return new OrderHistory(source);
        }

        @Override
        public OrderHistory[] newArray(int size) {
            return new OrderHistory[size];
        }
    };
}
