package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductTransactionDetailModel {
    @Expose
    private int id;

    @Expose
    private int createdBy;

    @Expose
    private int itemQty;

    @Expose
    @SerializedName("Products_id")
    private int productId;
}
