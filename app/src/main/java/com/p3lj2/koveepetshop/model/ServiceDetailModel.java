package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ServiceDetailModel implements Parcelable {
    @Expose
    private int id;

    @Expose
    private double price;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    @SerializedName("PetSizes_id")
    private int petSizeId;

    @Expose
    @SerializedName("PetTypes_id")
    private int petTypeId;

    @Expose
    @SerializedName("Services_id")
    private int serviceId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.price);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.petSizeId);
        dest.writeInt(this.petTypeId);
        dest.writeInt(this.serviceId);
    }

    protected ServiceDetailModel(Parcel in) {
        this.id = in.readInt();
        this.price = in.readDouble();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.petSizeId = in.readInt();
        this.petTypeId = in.readInt();
        this.serviceId = in.readInt();
    }

    public static final Parcelable.Creator<ServiceDetailModel> CREATOR = new Parcelable.Creator<ServiceDetailModel>() {
        @Override
        public ServiceDetailModel createFromParcel(Parcel source) {
            return new ServiceDetailModel(source);
        }

        @Override
        public ServiceDetailModel[] newArray(int size) {
            return new ServiceDetailModel[size];
        }
    };
}
