package com.shoppin.shopper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 1/9/16.
 */

public class OrderRequest implements Parcelable {

    @SerializedName("order_number")
    public String order_number;
    @SerializedName("order_slot_id")
    public String order_slot_id;
    @SerializedName("shipping_address_id")
    public String shipping_address_id;
    @SerializedName("total")
    public String total;
    @SerializedName("status")
    public String status;
    @SerializedName("address1")
    public String address1;
    @SerializedName("suburb_name")
    public String suburb_name;
    @SerializedName("delivery_date")
    public String delivery_date;
    @SerializedName("delivery_time")
    public String delivery_time;
    @SerializedName("customer_name")
    public String customerName;
    @SerializedName("item_count")
    public String itemCount;


    public OrderRequest() {
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
        dest.writeString(this.status);
        dest.writeString(this.address1);
        dest.writeString(this.suburb_name);
        dest.writeString(this.delivery_date);
        dest.writeString(this.delivery_time);
        dest.writeString(this.customerName);
        dest.writeString(this.itemCount);
    }

    protected OrderRequest(Parcel in) {
        this.order_number = in.readString();
        this.order_slot_id = in.readString();
        this.shipping_address_id = in.readString();
        this.total = in.readString();
        this.status = in.readString();
        this.address1 = in.readString();
        this.suburb_name = in.readString();
        this.delivery_date = in.readString();
        this.delivery_time = in.readString();
        this.customerName = in.readString();
        this.itemCount = in.readString();
    }

    public static final Creator<OrderRequest> CREATOR = new Creator<OrderRequest>() {
        @Override
        public OrderRequest createFromParcel(Parcel source) {
            return new OrderRequest(source);
        }

        @Override
        public OrderRequest[] newArray(int size) {
            return new OrderRequest[size];
        }
    };
}
