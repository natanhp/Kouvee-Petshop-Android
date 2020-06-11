package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.LogRepository;

import java.util.List;

public class LogViewModel extends AndroidViewModel {
    private LogRepository logRepository;
    private EmployeeRepository employeeRepository;

    public LogViewModel(@NonNull Application application) {
        super(application);
        logRepository = new LogRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<ProductModel>> getProduct(String bearerToken) {
        return logRepository.getProduct(bearerToken);
    }

    public LiveData<List<SupplierModel>> getSupplier(String bearerToken) {
        return logRepository.getSupplier(bearerToken);
    }

    public LiveData<List<EmployeeModel>> logEmployee(String bearerToken) {
        return logRepository.logEmployee(bearerToken);
    }

    public LiveData<Boolean> getIsLoading() {
        return logRepository.getIsLoading();
    }

    public LiveData<Object[]> getIsSuccess() {
        return logRepository.getIsSuccess();
    }

    public LiveData<EmployeeModel> employeeModel() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
