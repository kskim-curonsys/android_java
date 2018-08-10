package com.curonsys.android_java.data;

import android.os.Parcel;
import android.os.Parcelable;

public class BasicInfo implements Parcelable {
    int status;

    public int getStatus() {
        return status;
    }

    public BasicInfo() {
    }

    public BasicInfo(Parcel source) {
        status = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
    }

    public static Creator<BasicInfo> CREATOR = new Creator<BasicInfo>() {
        @Override
        public BasicInfo createFromParcel(Parcel source) {
            return new BasicInfo(source);
        }

        @Override
        public BasicInfo[] newArray(int size) {
            return new BasicInfo[size];
        }
    };
}
