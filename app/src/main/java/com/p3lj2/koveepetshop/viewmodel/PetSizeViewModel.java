package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.PetSizeRepository;

import java.util.List;

public class PetSizeViewModel extends AndroidViewModel {
    private PetSizeRepository petSizeRepository;
    private EmployeeRepository employeeRepository;

    public PetSizeViewModel(@NonNull Application application) {
        super(application);

        petSizeRepository = new PetSizeRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<PetSizeModel>> getAll(String bearerToken) {
        return petSizeRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getIsLoading() {
        return petSizeRepository.getIsLoading();
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}