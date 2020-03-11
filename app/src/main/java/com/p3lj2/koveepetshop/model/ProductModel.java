package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class ProductModel {
    @Expose
    private int id;

    @Expose
    private String productName;

    @Expose
    private int productQuantity;

    @Expose
    private double productPrice;

    @Expose
    private String meassurement;

    @Expose
    private int createdBy;

    @Expose
    private int minimumQty;
}
