package com.shoppin.shoper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.shoppin.shoper.utils.Utils;

import java.util.ArrayList;

import static android.R.id.list;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by ubuntu on 5/9/16.
 */

public class Product implements Parcelable {


    @SerializedName("order_item_id")
    public String productItemId;
    @SerializedName("product_id")
    public String productId;
    @SerializedName("product_name")
    public String productName;
    @SerializedName("images")
    public String[] images;
    @SerializedName("availability")
    public int productAvailability;
    @SerializedName("quantity")
    public String productQuntity;
    @SerializedName("price")
    public String productPrice;
    @SerializedName("saleprice")
    public String productSalePrice;


    @SerializedName("option_list")
    public ArrayList<ProductOption> productOptionArrayList;

    private String selectedOptions = null;

    public Product() {
    }

    public String getSelectedOptions() {
        if (Utils.isNullOrEmpty(selectedOptions)) {
            StringBuilder sb = new StringBuilder();
            boolean isFirstTime = true;
            for (int i = 0; i < productOptionArrayList.size(); i++) {
                if (isFirstTime) {
                    sb.append(productOptionArrayList.get(i).optionName + ":" + productOptionArrayList.get(i).value);
                    isFirstTime = false;
                }else{

                    sb.append("\n"+productOptionArrayList.get(i).optionName + ":" + productOptionArrayList.get(i).value);
                }

            }
            selectedOptions = sb.toString();
        }


        return selectedOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productItemId);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeStringArray(this.images);
        dest.writeInt(this.productAvailability);
        dest.writeString(this.productQuntity);
        dest.writeString(this.productPrice);
        dest.writeString(this.productSalePrice);
        dest.writeTypedList(this.productOptionArrayList);
        dest.writeString(this.selectedOptions);
    }

    protected Product(Parcel in) {
        this.productItemId = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.images = in.createStringArray();
        this.productAvailability = in.readInt();
        this.productQuntity = in.readString();
        this.productPrice = in.readString();
        this.productSalePrice = in.readString();
        this.productOptionArrayList = in.createTypedArrayList(ProductOption.CREATOR);
        this.selectedOptions = in.readString();
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
