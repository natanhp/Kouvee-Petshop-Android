package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.PetTypeRepository;

import java.util.List;

public class PetTypeViewModel extends AndroidViewModel {
    private PetTypeRepository petTypeRepository;
    private EmployeeRepository employeeRepository;

    public PetTypeViewModel(Application application) {
        super(application);
        petTypeRepository = new PetTypeRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<PetTypeModel>> getAll(String bearerToken) {
        return petTypeRepository.getAll(bearerToken);
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getIsLoading() {
        return petTypeRepository.getIsLoading();
    }

    public LiveData<Boolean> getIsLoadingEmployee() {
        return employeeRepository.getIsLoading();
    }
}
