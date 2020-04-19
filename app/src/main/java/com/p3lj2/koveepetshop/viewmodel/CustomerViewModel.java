package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.repository.CustomerRepository;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;

    public CustomerViewModel(@NonNull Application application) {
        super(application);

        customerRepository = new CustomerRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<CustomerModel>> getAll(String bearerToken) {
        return customerRepository.getAll(bearerToken);
    }

    public void insert(String bearerToken, CustomerModel customerModel) {
        customerRepository.insert(bearerToken, customerModel);
    }

    public void update(String bearerToken, CustomerModel customerModel) {
        customerRepository.update(bearerToken, customerModel);
    }

    public void delete(String bearerToken, int customerId, int csId) {
        customerRepository.delete(bearerToken, customerId, csId);
    }

    public LiveData<List<CustomerModel>> search(String bearerToken, String customerName) {
        return customerRepository.search(bearerToken, customerName);
    }

    public LiveData<Boolean> getIsLoading() {
        return customerRepository.getIsLoading();
    }

    public LiveData<EmployeeModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
