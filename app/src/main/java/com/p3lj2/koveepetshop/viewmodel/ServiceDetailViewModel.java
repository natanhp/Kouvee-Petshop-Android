package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.PetSizeRepository;
import com.p3lj2.koveepetshop.repository.PetTypeRepository;
import com.p3lj2.koveepetshop.repository.ServiceDetailRepository;
import com.p3lj2.koveepetshop.repository.ServiceRepository;

import java.util.List;

public class ServiceDetailViewModel extends AndroidViewModel {
    private ServiceDetailRepository serviceDetailRepository;
    private EmployeeRepository employeeRepository;
    private ServiceRepository serviceRepository;
    private PetTypeRepository petTypeRepository;
    private PetSizeRepository petSizeRepository;

    public ServiceDetailViewModel(@NonNull Application application) {
        super(application);

        serviceDetailRepository = new ServiceDetailRepository();
        employeeRepository = new EmployeeRepository(application);
        serviceRepository = new ServiceRepository();
        petTypeRepository = new PetTypeRepository();
        petSizeRepository = new PetSizeRepository();
    }

    public LiveData<List<ServiceDetailComplete>> getAll() {
        return serviceDetailRepository.getAll();
    }

    public void insert(String bearerToken, ServiceDetailModel serviceDetailModel) {
        serviceDetailRepository.insert(bearerToken, serviceDetailModel);
    }

    public void update(String bearerToken, ServiceDetailModel serviceDetailModel) {
        serviceDetailRepository.update(bearerToken, serviceDetailModel);
    }

    public void delete(String bearerToken, int serviceDetailId) {
        serviceDetailRepository.delete(bearerToken, serviceDetailId);
    }

    public LiveData<Boolean> getIsLoading() {
        return serviceDetailRepository.getIsLoading();
    }

    public LiveData<EmployeeModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }

    public LiveData<List<ServiceModel>> getAllServices(String bearerToken) {
        return serviceRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getServiceIsLoading() {
        return serviceRepository.getIsLoading();
    }

    public LiveData<List<PetTypeModel>> getAllPetTypes(String bearerToken) {
        return petTypeRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getPetTypeIsLoading() {
        return petTypeRepository.getIsLoading();
    }

    public LiveData<List<PetSizeModel>> getAllPetSizes(String bearerToken) {
        return petSizeRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getPetSizeIsLoading() {
        return petSizeRepository.getIsLoading();
    }
}
