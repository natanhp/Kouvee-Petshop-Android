package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductResponseModel implements Parcelable {

    @Expose
    @SerializedName("product")
    private ProductModel productModel;

    @Expose
    @SerializedName("image_url")
    private String imageUrl;

    public ProductResponseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productModel, flags);
        dest.writeString(this.imageUrl);
    }

    protected ProductResponseModel(Parcel in) {
        this.productModel = in.readParcelable(ProductModel.class.getClassLoader());
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<ProductResponseModel> CREATOR = new Parcelable.Creator<ProductResponseModel>() {
        @Override
        public ProductResponseModel createFromParcel(Parcel source) {
            return new ProductResponseModel(source);
        }

        @Override
        public ProductResponseModel[] newArray(int size) {
            return new ProductResponseModel[size];
        }
    };
}
