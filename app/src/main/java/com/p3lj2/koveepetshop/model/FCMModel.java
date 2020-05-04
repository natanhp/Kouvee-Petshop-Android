package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FCMModel {

    @Expose
    private String token;

    @SerializedName("employee_id")
    @Expose
    private int employeeId;
}
