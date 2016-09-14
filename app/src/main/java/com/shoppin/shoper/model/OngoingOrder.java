package com.shoppin.shoper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 6/9/16.
 */

public class OngoingOrder implements Parcelable{

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
    @SerializedName("status_label")
    public String statusLable;
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



    public OngoingOrder() {
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
        dest.writeString(this.statusLable);
        dest.writeString(this.address1);
        dest.writeString(this.suburb_name);
        dest.writeString(this.delivery_date);
        dest.writeString(this.delivery_time);
        dest.writeString(this.customerName);
        dest.writeString(this.itemCount);
    }

    protected OngoingOrder(Parcel in) {
        this.order_number = in.readString();
        this.order_slot_id = in.readString();
        this.shipping_address_id = in.readString();
        this.total = in.readString();
        this.status = in.readInt();
        this.statusLable = in.readString();
        this.address1 = in.readString();
        this.suburb_name = in.readString();
        this.delivery_date = in.readString();
        this.delivery_time = in.readString();
        this.customerName = in.readString();
        this.itemCount = in.readString();
    }

    public static final Creator<OngoingOrder> CREATOR = new Creator<OngoingOrder>() {
        @Override
        public OngoingOrder createFromParcel(Parcel source) {
            return new OngoingOrder(source);
        }

        @Override
        public OngoingOrder[] newArray(int size) {
            return new OngoingOrder[size];
        }
    };
}
