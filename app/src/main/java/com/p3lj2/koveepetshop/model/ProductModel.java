package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class ProductModel implements Parcelable {
    @Expose
    private int id;

    @Expose
    private String productName;

    @Expose
    private int productQuantity;

    @Expose
    private double productPrice;

    @Expose
    private String meassurement;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int minimumQty;

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

    public ProductModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.productName);
        dest.writeInt(this.productQuantity);
        dest.writeDouble(this.productPrice);
        dest.writeString(this.meassurement);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.minimumQty);
        dest.writeParcelable(this.creator, flags);
        dest.writeParcelable(this.updater, flags);
        dest.writeParcelable(this.deletor, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    protected ProductModel(Parcel in) {
        this.id = in.readInt();
        this.productName = in.readString();
        this.productQuantity = in.readInt();
        this.productPrice = in.readDouble();
        this.meassurement = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.minimumQty = in.readInt();
        this.creator = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.updater = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.deletor = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
}
