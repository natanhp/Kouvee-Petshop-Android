package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ServiceTransactionModel {
    @Expose
    private int createdBy;

    @Expose
    private double total;

    @Expose
    private List<ServiceTransactionDetailModel> serviceTransactionDetails;

    @Expose
    @SerializedName("Pets_id")
    private int petId;
}
