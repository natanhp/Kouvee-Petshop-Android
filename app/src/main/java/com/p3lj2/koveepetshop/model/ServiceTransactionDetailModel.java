package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ServiceTransactionDetailModel {
    @Expose
    @SerializedName("ServiceDetails_id")
    private int serviceDetailId;

    @Expose
    private int createdBy;
}
