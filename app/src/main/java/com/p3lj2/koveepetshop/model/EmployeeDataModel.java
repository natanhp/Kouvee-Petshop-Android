package com.p3lj2.koveepetshop.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "employee_table")
public class EmployeeDataModel {

    @PrimaryKey
    private int id;

    private String name;
    private String address;
    private String dateBirth;
    private String phoneNumber;
    private String role;
    private String username;
    private String password;
    private String token;
}
