package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    }

    protected PetModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.dateBirth = in.readString();
        this.customerId = in.readInt();
        this.petSizeId = in.readInt();
        this.petTypeId = in.readInt();
    }

    public static final Parcelable.Creator<PetModel> CREATOR = new Parcelable.Creator<PetModel>() {
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
