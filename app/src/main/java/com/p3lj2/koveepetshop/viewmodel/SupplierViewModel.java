package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.SupplierRepository;

import java.util.List;

public class SupplierViewModel extends AndroidViewModel {
    private SupplierRepository supplierRepository;
    private EmployeeRepository employeeRepository;

    public SupplierViewModel(@NonNull Application application) {
        super(application);

        supplierRepository = new SupplierRepository();
        employeeRepository = new EmployeeRepository(application);
    }

    public LiveData<List<SupplierModel>> getAll(String bearerToken) {
        return supplierRepository.getAll(bearerToken);
    }

    public void insert(String bearerToken, SupplierModel supplierModel) {
        supplierRepository.insert(bearerToken, supplierModel);
    }

    public void update(String bearerToken, SupplierModel supplierModel) {
        supplierRepository.update(bearerToken, supplierModel);
    }

    public void delete(String bearerToken, int supplierId, int ownerId) {
        supplierRepository.delete(bearerToken, supplierId, ownerId);
    }

    public LiveData<Boolean> getIsLoading() {
        return supplierRepository.getIsLoading();
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }
}
