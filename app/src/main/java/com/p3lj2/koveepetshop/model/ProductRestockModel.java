package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ProductRestockModel {
    @Expose
    @SerializedName("Suppliers_id")
    private int supplierId;

    @Expose
    private int createdBy;

    @Expose
    private int isArrived;

    @Expose
    private List<ProductRestockDetail> productRestockDetails;
}
