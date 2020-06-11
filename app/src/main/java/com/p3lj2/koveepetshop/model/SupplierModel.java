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

    @Expose(serialize = false)
    private EmployeeModel creator;

    @Expose(serialize = false)
    private EmployeeModel updater;

    @Expose(serialize = false)
    private EmployeeModel deletor;

    @Expose(serialize = false)
    private String createdAt;

    @Expose(serialize = false)
    private String updatedAt;

    @Expose(serialize = false)
    private String deletedAt;

    public SupplierModel() {
    }

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
        dest.writeParcelable(this.creator, flags);
        dest.writeParcelable(this.updater, flags);
        dest.writeParcelable(this.deletor, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    protected SupplierModel(Parcel in) {
        this.idSupplier = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
        this.creator = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.updater = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.deletor = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Creator<SupplierModel> CREATOR = new Creator<SupplierModel>() {
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
