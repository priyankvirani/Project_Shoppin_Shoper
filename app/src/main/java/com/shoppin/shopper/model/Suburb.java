package com.shoppin.shopper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 12/8/16.
 */

public class Suburb implements Parcelable{
    @SerializedName("suburb_id")
    public String suburb_id;
    @SerializedName("suburb_name")
    public String suburb_name;
    public boolean selected;

    @Override
    public String toString() {
        return suburb_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.suburb_id);
        dest.writeString(this.suburb_name);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public Suburb() {
    }

    protected Suburb(Parcel in) {
        this.suburb_id = in.readString();
        this.suburb_name = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<Suburb> CREATOR = new Creator<Suburb>() {
        @Override
        public Suburb createFromParcel(Parcel source) {
            return new Suburb(source);
        }

        @Override
        public Suburb[] newArray(int size) {
            return new Suburb[size];
        }
    };
}
