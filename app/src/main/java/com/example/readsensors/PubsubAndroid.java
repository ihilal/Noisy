package com.example.readsensors;

import android.os.Parcel;
import android.os.Parcelable;

public class PubsubAndroid extends  PubSub implements Parcelable {


    public PubsubAndroid(String host) {

        super(host);
    }




    public PubsubAndroid(Parcel in) {

        super(in.readString());

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(super.getHost());

    }

    public static final Parcelable.Creator<PubsubAndroid> CREATOR = new Parcelable.Creator<PubsubAndroid>() {

        public PubsubAndroid createFromParcel(Parcel in) {
            return new PubsubAndroid(in);
        }

        public PubsubAndroid[] newArray(int size) {
            return new PubsubAndroid[size];
        }
    };





}
