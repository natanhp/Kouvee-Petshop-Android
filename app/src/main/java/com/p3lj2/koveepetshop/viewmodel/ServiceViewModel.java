package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.ServiceRepository;

import java.util.List;

public class ServiceViewModel extends AndroidViewModel {
    private ServiceRepository serviceRepository;
    private EmployeeRepository employeeRepository;

    public ServiceViewModel(@NonNull Application application) {
        super(application);

        serviceRepository = new ServiceRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<ServiceModel>> getAll(String bearerToken) {
        return serviceRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getIsLoading() {
        return serviceRepository.getIsLoading();
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
