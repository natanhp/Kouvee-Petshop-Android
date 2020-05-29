package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ProductTransactionModel implements Parcelable {

    @Expose
    private String id;

    @Expose
    private double total;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    @SerializedName("Customers_id")
    private Integer customerId;

    @Expose
    private int isPaid;

    @Expose
    private List<ProductTransactionDetailModel> productTransactionDetails;

    @Expose(serialize = false)
    @SerializedName("cs_name")
    private String csName;

    @Expose(serialize = false)
    private CustomerModel customer;

    @Expose(serialize = false)
    private String createdAt;

    public ProductTransactionModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeDouble(this.total);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeValue(this.customerId);
        dest.writeInt(this.isPaid);
        dest.writeTypedList(this.productTransactionDetails);
        dest.writeString(this.csName);
        dest.writeParcelable(this.customer, flags);
        dest.writeString(this.createdAt);
    }

    protected ProductTransactionModel(Parcel in) {
        this.id = in.readString();
        this.total = in.readDouble();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.customerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isPaid = in.readInt();
        this.productTransactionDetails = in.createTypedArrayList(ProductTransactionDetailModel.CREATOR);
        this.csName = in.readString();
        this.customer = in.readParcelable(CustomerModel.class.getClassLoader());
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<ProductTransactionModel> CREATOR = new Parcelable.Creator<ProductTransactionModel>() {
        @Override
        public ProductTransactionModel createFromParcel(Parcel source) {
            return new ProductTransactionModel(source);
        }

        @Override
        public ProductTransactionModel[] newArray(int size) {
            return new ProductTransactionModel[size];
        }
    };
}
