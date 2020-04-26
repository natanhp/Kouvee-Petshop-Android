package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductRestockDetail {
    @Expose
    @SerializedName("Products_id")
    private int productId;

    @Expose
    private int itemQty;

    @Expose
    private int createdBy;
}
