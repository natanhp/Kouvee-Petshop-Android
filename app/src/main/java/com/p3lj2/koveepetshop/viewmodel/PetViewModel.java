package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.PetComplete;
import com.p3lj2.koveepetshop.model.PetModel;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.repository.CustomerRepository;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.PetRepository;
import com.p3lj2.koveepetshop.repository.PetSizeRepository;
import com.p3lj2.koveepetshop.repository.PetTypeRepository;

import java.util.List;

public class PetViewModel extends AndroidViewModel {
    private PetRepository petRepository;
    private EmployeeRepository employeeRepository;
    private PetTypeRepository petTypeRepository;
    private PetSizeRepository petSizeRepository;
    private CustomerRepository customerRepository;

    public PetViewModel(@NonNull Application application) {
        super(application);

        petRepository = new PetRepository();
        employeeRepository = new EmployeeRepository(application);
        petTypeRepository = new PetTypeRepository();
        petSizeRepository = new PetSizeRepository();
        customerRepository = new CustomerRepository();
    }

    public LiveData<List<PetComplete>> getAll(String bearerToken) {
        return petRepository.getAll(bearerToken);
    }

    public void insert(String bearerToken, PetModel petModel) {
        petRepository.insert(bearerToken, petModel);
    }

    public void update(String bearerToken, PetModel petModel) {
        petRepository.update(bearerToken, petModel);
    }

    public void delete(String bearerToken, int petId, int csId) {
        petRepository.delete(bearerToken, petId, csId);
    }

    public LiveData<List<PetComplete>> search(String bearerToken, String petName) {
        return petRepository.search(bearerToken, petName);
    }

    public LiveData<Boolean> getIsLoading() {
        return petRepository.getIsLoading();
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
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

    public LiveData<List<CustomerModel>> getAllCustomers(String bearerToken) {
        return customerRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getCustomerIsLoading() {
        return customerRepository.getIsLoading();
    }
}
