package com.shoppin.shopper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 6/9/16.
 */

public class ProductOption implements Parcelable{

    @SerializedName("option_id")
    public String optionId;
    @SerializedName("order_item_id")
    public String orderItemId;
    @SerializedName("option_name")
    public String optionName;
    @SerializedName("value")
    public String value;
    @SerializedName("order_id")
    public String orderId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionId);
        dest.writeString(this.orderItemId);
        dest.writeString(this.optionName);
        dest.writeString(this.value);
        dest.writeString(this.orderId);
    }

    public ProductOption() {
    }

    protected ProductOption(Parcel in) {
        this.optionId = in.readString();
        this.orderItemId = in.readString();
        this.optionName = in.readString();
        this.value = in.readString();
        this.orderId = in.readString();
    }

    public static final Creator<ProductOption> CREATOR = new Creator<ProductOption>() {
        @Override
        public ProductOption createFromParcel(Parcel source) {
            return new ProductOption(source);
        }

        @Override
        public ProductOption[] newArray(int size) {
            return new ProductOption[size];
        }
    };
}
