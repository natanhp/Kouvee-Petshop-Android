package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ServiceDetailComplete implements Parcelable {

    @Expose
    @SerializedName("service_detail")
    private ServiceDetailModel serviceDetailModel;

    @Expose
    @SerializedName("complete_name")
    private String serviceCompleteName;

    public ServiceDetailComplete() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.serviceDetailModel, flags);
        dest.writeString(this.serviceCompleteName);
    }

    protected ServiceDetailComplete(Parcel in) {
        this.serviceDetailModel = in.readParcelable(ServiceDetailModel.class.getClassLoader());
        this.serviceCompleteName = in.readString();
    }

    public static final Parcelable.Creator<ServiceDetailComplete> CREATOR = new Parcelable.Creator<ServiceDetailComplete>() {
        @Override
        public ServiceDetailComplete createFromParcel(Parcel source) {
            return new ServiceDetailComplete(source);
        }

        @Override
        public ServiceDetailComplete[] newArray(int size) {
            return new ServiceDetailComplete[size];
        }
    };
}
