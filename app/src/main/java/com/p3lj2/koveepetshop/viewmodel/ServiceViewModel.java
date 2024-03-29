package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeModel;
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

    public void insert(String bearerToken, ServiceModel serviceModel) {
        serviceRepository.insert(bearerToken, serviceModel);
    }

    public void update(String bearerToken, ServiceModel serviceModel) {
        serviceRepository.update(bearerToken, serviceModel);
    }

    public void delete(String bearerToken, int serviceId, int ownerId) {
        serviceRepository.delete(bearerToken, serviceId, ownerId);
    }

    public LiveData<List<ServiceModel>> search(String bearerToken, String serviceName) {
        return serviceRepository.search(bearerToken, serviceName);
    }

    public LiveData<Boolean> getIsLoading() {
        return serviceRepository.getIsLoading();
    }

    public LiveData<EmployeeModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
