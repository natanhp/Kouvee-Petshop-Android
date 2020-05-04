package com.p3lj2.koveepetshop.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.p3lj2.koveepetshop.model.EmployeeModel;

@Dao
public interface EmployeeDAO {
    @Insert
    void insert(EmployeeModel employeeDataModel);

    @Delete
    void delete(EmployeeModel employeeDataModel);

    @Query("SELECT * FROM employee_table LIMIT 1")
    EmployeeModel getEmployee();
}
