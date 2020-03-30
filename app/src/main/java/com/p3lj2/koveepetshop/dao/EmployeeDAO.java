package com.p3lj2.koveepetshop.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;

@Dao
public interface EmployeeDAO {
    @Insert
    void insert(EmployeeDataModel employeeDataModel);

    @Delete
    void delete(EmployeeDataModel employeeDataModel);

    @Query("SELECT * FROM employee_table LIMIT 1")
    EmployeeDataModel getEmployee();
}
