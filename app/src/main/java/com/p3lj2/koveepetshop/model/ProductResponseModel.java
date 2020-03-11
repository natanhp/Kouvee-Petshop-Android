package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductResponseModel {

    @Expose
    @SerializedName("product")
    private ProductModel productModel;

    @Expose
    @SerializedName("image_url")
    private String imageUrl;
}
