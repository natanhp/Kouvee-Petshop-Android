package com.p3lj2.koveepetshop.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;

@Dao
public interface EmployeeDAO {
    @Insert
    void insert(EmployeeDataModel employeeDataModel);
}
