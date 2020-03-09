package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EmployeeLoginSchema {

    @Expose
    private String message;

    @Expose
    @SerializedName("data")
    private EmployeeModel employee;

    @Expose
    private String token;
}
