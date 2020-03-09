package com.p3lj2.koveepetshop.dao;

import androidx.room.Insert;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;

public interface EmployeeDAO {
    @Insert
    void insert(EmployeeDataModel employeeDataModel);
}
