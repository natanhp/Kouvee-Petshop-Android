package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductTransactionDetailModel implements Parcelable {
    @Expose
    private int id;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int itemQty;

    @Expose
    @SerializedName("Products_id")
    private int productId;

    @Expose(serialize = false)
    @SerializedName("product")
    private ProductModel productModel;

    public ProductTransactionDetailModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.itemQty);
        dest.writeInt(this.productId);
        dest.writeParcelable(this.productModel, flags);
    }

    protected ProductTransactionDetailModel(Parcel in) {
        this.id = in.readInt();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.itemQty = in.readInt();
        this.productId = in.readInt();
        this.productModel = in.readParcelable(ProductModel.class.getClassLoader());
    }

    public static final Creator<ProductTransactionDetailModel> CREATOR = new Creator<ProductTransactionDetailModel>() {
        @Override
        public ProductTransactionDetailModel createFromParcel(Parcel source) {
            return new ProductTransactionDetailModel(source);
        }

        @Override
        public ProductTransactionDetailModel[] newArray(int size) {
            return new ProductTransactionDetailModel[size];
        }
    };
}
