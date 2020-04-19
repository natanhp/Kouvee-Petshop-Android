package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.FCMModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.FCMRepository;

public class EmployeeViewModel extends AndroidViewModel {
    private EmployeeRepository employeeRepository;
    private FCMRepository fcmRepository;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        employeeRepository = new EmployeeRepository(application);
        fcmRepository = new FCMRepository();
    }

    public LiveData<EmployeeModel> login(String username, String password) {
        return employeeRepository.login(username, password);
    }

    public LiveData<EmployeeModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public void delete(EmployeeModel employeeDataModel) {
        employeeRepository.delete(employeeDataModel);
    }

    public LiveData<Boolean> getIsLoading() {
        return employeeRepository.getIsLoading();
    }

    public void insertFCMToken(String bearerToken, FCMModel fcmModel) {
        fcmRepository.insert(bearerToken, fcmModel);
    }

    public void deleteFCMToken(String bearerToken, String token) {
        fcmRepository.delete(bearerToken, token);
    }

    public LiveData<Boolean> getFCMIsLoading() {
        return fcmRepository.getIsLoading();
    }
}
