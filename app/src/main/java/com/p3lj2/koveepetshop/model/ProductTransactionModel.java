package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ProductTransactionModel {

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
    private List<ProductTransactionDetailModel> productTransactionkDetails;

    @Expose(serialize = false)
    @SerializedName("cs_name")
    private String csName;

    @Expose(serialize = false)
    @SerializedName("customer_name")
    private String customerName;
}
