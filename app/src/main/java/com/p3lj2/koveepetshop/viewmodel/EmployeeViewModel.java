package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;

public class EmployeeViewModel extends AndroidViewModel {
    private EmployeeRepository employeeRepository;
    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        employeeRepository = new EmployeeRepository(application);
    }

    public void login(String username, String password) {
        employeeRepository.login(username, password);
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
