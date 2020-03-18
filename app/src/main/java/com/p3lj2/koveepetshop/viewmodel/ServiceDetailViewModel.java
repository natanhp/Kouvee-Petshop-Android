package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.ServiceDetailRepository;

import java.util.List;

public class ServiceDetailViewModel extends AndroidViewModel {
    private ServiceDetailRepository serviceDetailRepository;
    private EmployeeRepository employeeRepository;

    public ServiceDetailViewModel(@NonNull Application application) {
        super(application);

        serviceDetailRepository = new ServiceDetailRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<ServiceDetailComplete>> getAll() {
        return serviceDetailRepository.getAll();
    }

    public LiveData<Boolean> getIsLoading() {
        return serviceDetailRepository.getIsLoading();
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
