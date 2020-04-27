package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ProductRestockModel {
    @Expose
    private String id;

    @Expose
    @SerializedName("Suppliers_id")
    private int supplierId;

    @Expose
    private int createdBy;

    @Expose
    private int isArrived;

    @Expose(serialize = false)
    @SerializedName("supplier_name")
    private String supplierName;

    @Expose
    private List<ProductRestockDetail> productRestockDetails;
}
