package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PetModel implements Parcelable {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String dateBirth;

    @Expose
    @SerializedName("Customers_id")
    private int customerId;

    @Expose
    @SerializedName("PetSizes_id")
    private int petSizeId;

    @Expose
    @SerializedName("PetTypes_id")
    private int petTypeId;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int deletedBy;

    @Expose(serialize = false)
    @SerializedName("service_details")
    private List<ServiceDetailModel> serviceDetails;

    public PetModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.dateBirth);
        dest.writeInt(this.customerId);
        dest.writeInt(this.petSizeId);
        dest.writeInt(this.petTypeId);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.deletedBy);
        dest.writeTypedList(this.serviceDetails);
    }

    protected PetModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.dateBirth = in.readString();
        this.customerId = in.readInt();
        this.petSizeId = in.readInt();
        this.petTypeId = in.readInt();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
        this.serviceDetails = in.createTypedArrayList(ServiceDetailModel.CREATOR);
    }

    public static final Creator<PetModel> CREATOR = new Creator<PetModel>() {
        @Override
        public PetModel createFromParcel(Parcel source) {
            return new PetModel(source);
        }

        @Override
        public PetModel[] newArray(int size) {
            return new PetModel[size];
        }
    };
}
