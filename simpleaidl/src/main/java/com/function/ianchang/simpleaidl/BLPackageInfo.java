package com.function.ianchang.simpleaidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ianchang on 2017/12/25.
 */

public class BLPackageInfo implements Parcelable{

    public String name;
    public BLPackageInfo(){}

    public BLPackageInfo(Parcel in){
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static Creator<BLPackageInfo> CREATOR = new Creator<BLPackageInfo>() {
        @Override
        public BLPackageInfo createFromParcel(Parcel source) {
            return new BLPackageInfo(source);
        }

        @Override
        public BLPackageInfo[] newArray(int size) {
            return new BLPackageInfo[0];
        }
    };

}
