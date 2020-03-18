package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class ServiceModel implements Parcelable {

    @Expose
    private int id;

    @Expose
    private String serviceName;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int deletedBy;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.serviceName);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.deletedBy);
    }

    protected ServiceModel(Parcel in) {
        this.id = in.readInt();
        this.serviceName = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
    }

    public static final Parcelable.Creator<ServiceModel> CREATOR = new Parcelable.Creator<ServiceModel>() {
        @Override
        public ServiceModel createFromParcel(Parcel source) {
            return new ServiceModel(source);
        }

        @Override
        public ServiceModel[] newArray(int size) {
            return new ServiceModel[size];
        }
    };
}
