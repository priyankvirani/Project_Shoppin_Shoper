package com.shoppin.shoper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 5/9/16.
 */

public class Product implements Parcelable {
    public String order_number;

    @SerializedName("product_item_id")
    public String product_item_id;
    @SerializedName("product_id")
    public String product_id;
    @SerializedName("saleprice_1")
    public String saleprice_1;
    @SerializedName("product_name")
    public String product_name;
    @SerializedName("images")
    public String[] images;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_number);
        dest.writeString(this.product_item_id);
        dest.writeString(this.product_id);
        dest.writeString(this.saleprice_1);
        dest.writeString(this.product_name);
        dest.writeStringArray(this.images);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.order_number = in.readString();
        this.product_item_id = in.readString();
        this.product_id = in.readString();
        this.saleprice_1 = in.readString();
        this.product_name = in.readString();
        this.images = in.createStringArray();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
