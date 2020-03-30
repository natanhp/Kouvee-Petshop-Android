package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repository;
    private EmployeeRepository employeeRepository;

    public ProductViewModel(Application application) {
        super(application);
        this.repository = new ProductRepository();
        this.employeeRepository = new EmployeeRepository(application);
    }

    public void insert(String token, ProductResponseModel productResponseModel) {
        repository.insert(token, productResponseModel);
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeLoading() {
        return employeeRepository.getIsLoading();
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<List<ProductResponseModel>> getAll() {
        return repository.getAll();
    }

    public void delete(String token, int id, int ownerId) {
        repository.delete(token, id, ownerId);
    }

    public void update(String token, ProductResponseModel productResponseModel) {
        repository.update(token, productResponseModel);
    }

    public LiveData<List<ProductResponseModel>> getByName(String productName) {
        return repository.getByName(productName);
    }
}
