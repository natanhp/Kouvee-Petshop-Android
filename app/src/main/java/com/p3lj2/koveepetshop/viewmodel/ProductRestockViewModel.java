package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.ProductRepository;
import com.p3lj2.koveepetshop.repository.ProductRestockRepository;
import com.p3lj2.koveepetshop.repository.SupplierRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductRestockViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private SupplierRepository supplierRepository;
    private EmployeeRepository employeeRepository;
    private ProductRestockRepository productRestockRepository;
    private static MutableLiveData<List<ProductModel>> bookedProductModels = new MutableLiveData<>();
    private static MutableLiveData<HashMap<Integer, Integer>> viewPositions = new MutableLiveData<>();

    public ProductRestockViewModel(Application application) {
        super(application);
        this.productRepository = new ProductRepository();
        this.supplierRepository = new SupplierRepository();
        employeeRepository = new EmployeeRepository(application);
        productRestockRepository = new ProductRestockRepository();
    }

    public LiveData<List<ProductResponseModel>> getAllProduct() {
        return productRepository.getAll();
    }

    public void setBookedProductModels(ProductModel productModel) {
        List<ProductModel> bookedProductModelsTmp = new ArrayList<>();
        if (bookedProductModels.getValue() != null) {
            bookedProductModelsTmp.addAll(bookedProductModels.getValue());
        }
        bookedProductModelsTmp.add(productModel);
        bookedProductModels.postValue(bookedProductModelsTmp);
    }

    public LiveData<List<ProductModel>> getBookedProductModel() {
        return bookedProductModels;
    }

    public void setViewPositions(int productId, int position) {
        HashMap<Integer, Integer> viewPositionsTmp = new HashMap<>();
        if (viewPositions.getValue() != null) {
            viewPositionsTmp.putAll(viewPositions.getValue());
        }
        viewPositionsTmp.put(productId, position);
        viewPositions.postValue(viewPositionsTmp);
    }

    public void deleteBookedProductByPosition(int position) {
        List<ProductModel> bookedProductModelsTmp = new ArrayList<>();
        if (bookedProductModels.getValue() != null) {
            bookedProductModelsTmp.addAll(bookedProductModels.getValue());
        }
        deleteViewPositionById(bookedProductModelsTmp.get(position).getId());
        bookedProductModelsTmp.remove(position);
        bookedProductModels.postValue(bookedProductModelsTmp);
    }

    public void updateeBookedProductByPosition(int position, int quantity) {
        List<ProductModel> bookedProductModelsTmp = new ArrayList<>();
        if (bookedProductModels.getValue() != null) {
            bookedProductModelsTmp.addAll(bookedProductModels.getValue());
        }
        bookedProductModelsTmp.get(position).setProductQuantity(quantity);
        bookedProductModels.postValue(bookedProductModelsTmp);
    }

    public LiveData<HashMap<Integer, Integer>> getViewPositions() {
        return viewPositions;
    }

    public LiveData<Boolean> getProductIsLoading() {
        return productRepository.getIsLoading();
    }

    private void deleteViewPositionById(int id) {
        HashMap<Integer, Integer> viewPositionsTmp = new HashMap<>();
        if (viewPositions.getValue() != null) {
            viewPositionsTmp.putAll(viewPositions.getValue());
        }
        viewPositionsTmp.remove(id);
        viewPositions.setValue(viewPositionsTmp);
    }

    public LiveData<List<SupplierModel>> getAllSuppliers(String bearerToken) {
        return supplierRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getSupplierIsLoading() {
        return supplierRepository.getIsLoading();
    }

    public LiveData<EmployeeModel> getAllEmployees() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }

    public void insert(String bearer, ProductRestockModel productRestockModel) {
        productRestockRepository.insert(bearer, productRestockModel);
    }

    public LiveData<Boolean> getIsLoading() {
        return productRestockRepository.getIsLoading();
    }
}
