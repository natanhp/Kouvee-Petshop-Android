package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class CustomerModel implements Parcelable {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String dateBirth;

    @Expose
    private String phoneNumber;

    @Expose
    private String address;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int deletedBy;

    public CustomerModel() {
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
        dest.writeString(this.phoneNumber);
        dest.writeString(this.address);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.deletedBy);
    }

    protected CustomerModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.dateBirth = in.readString();
        this.phoneNumber = in.readString();
        this.address = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
    }

    public static final Parcelable.Creator<CustomerModel> CREATOR = new Parcelable.Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel source) {
            return new CustomerModel(source);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };
}
