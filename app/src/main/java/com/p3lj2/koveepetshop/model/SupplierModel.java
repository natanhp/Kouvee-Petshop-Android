package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class SupplierModel implements Parcelable {

    @Expose
    private int idSupplier;

    @Expose
    private String name;

    @Expose
    private String address;

    @Expose
    private String phoneNumber;

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
        dest.writeInt(this.idSupplier);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.deletedBy);
    }

    public SupplierModel() {
    }

    protected SupplierModel(Parcel in) {
        this.idSupplier = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
    }

    public static final Parcelable.Creator<SupplierModel> CREATOR = new Parcelable.Creator<SupplierModel>() {
        @Override
        public SupplierModel createFromParcel(Parcel source) {
            return new SupplierModel(source);
        }

        @Override
        public SupplierModel[] newArray(int size) {
            return new SupplierModel[size];
        }
    };
}
