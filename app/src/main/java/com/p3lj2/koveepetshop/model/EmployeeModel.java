package com.p3lj2.koveepetshop.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
@Entity(tableName = "employee_table")
public class EmployeeModel {

    @Expose
    @PrimaryKey
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

    @Expose(serialize = false)
    private String token;

}
