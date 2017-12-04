package com.elhaj.med.fbalbums.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by med on 12/4/17.
 */

public class Photo implements Parcelable {

    private String urlPhoto;

    public Photo(Parcel in) {
        urlPhoto = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Photo() {

    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(urlPhoto);
    }
}