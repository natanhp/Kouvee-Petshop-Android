package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class EmployeeModel {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String address;

    @Expose
    private String dateBirth;

    @Expose
    private String phoneNumber;

    @Expose
    private String role;

    @Expose
    private String username;

    @Expose
    private String password;
}
