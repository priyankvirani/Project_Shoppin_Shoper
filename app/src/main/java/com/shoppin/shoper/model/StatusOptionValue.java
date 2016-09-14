package com.shoppin.shoper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 10/9/16.
 */

public class StatusOptionValue implements Parcelable{
    @SerializedName("status")
    public int status;

    @SerializedName("status_label")
    public String statusLabel;

    public boolean isSelected;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.statusLabel);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public StatusOptionValue() {
    }

    protected StatusOptionValue(Parcel in) {
        this.status = in.readInt();
        this.statusLabel = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<StatusOptionValue> CREATOR = new Creator<StatusOptionValue>() {
        @Override
        public StatusOptionValue createFromParcel(Parcel source) {
            return new StatusOptionValue(source);
        }

        @Override
        public StatusOptionValue[] newArray(int size) {
            return new StatusOptionValue[size];
        }
    };
}
