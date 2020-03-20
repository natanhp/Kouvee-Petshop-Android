package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.PetComplete;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.PetRepository;

import java.util.List;

public class PetViewModel extends AndroidViewModel {
    private PetRepository petRepository;
    private EmployeeRepository employeeRepository;

    public PetViewModel(@NonNull Application application) {
        super(application);

        petRepository = new PetRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<PetComplete>> getAll(String bearerToken) {
        return petRepository.getAll(bearerToken);
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
}
