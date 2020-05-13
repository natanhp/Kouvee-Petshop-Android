package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.repository.CustomerRepository;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.ProductRepository;
import com.p3lj2.koveepetshop.repository.ProductTransactionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductTransactionViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private ProductTransactionRepository productTransactionRepository;
    private static MutableLiveData<HashMap<Integer, Integer>> viewPositions = new MutableLiveData<>();
    private static MutableLiveData<List<ProductModel>> cart = new MutableLiveData<>();

    public ProductTransactionViewModel(@NonNull Application application) {
        super(application);

        productRepository = new ProductRepository();
        customerRepository = new CustomerRepository();
        employeeRepository = new EmployeeRepository(application);
        productTransactionRepository = new ProductTransactionRepository();
    }

    public LiveData<List<ProductResponseModel>> getAllProducts() {
        return productRepository.getAll();
    }

    public LiveData<Boolean> getProductIsLoading() {
        return productRepository.getIsLoading();
    }

    public void setViewPositions(int productId, int position) {
        HashMap<Integer, Integer> viewPositionsTmp = new HashMap<>();
        if (viewPositions.getValue() != null) {
            viewPositionsTmp.putAll(viewPositions.getValue());
        }
        viewPositionsTmp.put(productId, position);
        viewPositions.postValue(viewPositionsTmp);
    }

    public LiveData<HashMap<Integer, Integer>> getViewPositions() {
        return viewPositions;
    }

    public void setCart(ProductModel productModel) {
        List<ProductModel> cartTmp = new ArrayList<>();
        if (cart.getValue() != null) {
            cartTmp.addAll(cart.getValue());
        }
        cartTmp.add(productModel);
        cart.postValue(cartTmp);
    }

    public LiveData<List<ProductModel>> getCart() {
        return cart;
    }

    public void updateCartByPosition(int position, int quantity) {
        List<ProductModel> cartTmp = new ArrayList<>();
        if (cart.getValue() != null) {
            cartTmp.addAll(cart.getValue());
        }
        cartTmp.get(position).setProductQuantity(quantity);
        cart.postValue(cartTmp);
    }

    public void deleteCartItemByPosition(int position) {
        List<ProductModel> cartTmp = new ArrayList<>();
        if (cart.getValue() != null) {
            cartTmp.addAll(cart.getValue());
        }
        deleteViewPositionById(cartTmp.get(position).getId());
        cartTmp.remove(position);
        cart.postValue(cartTmp);
    }

    private void deleteViewPositionById(int id) {
        HashMap<Integer, Integer> viewPositionsTmp = new HashMap<>();
        if (viewPositions.getValue() != null) {
            viewPositionsTmp.putAll(viewPositions.getValue());
        }
        viewPositionsTmp.remove(id);
        viewPositions.setValue(viewPositionsTmp);
    }

    public void resetCart() {
        viewPositions = new MutableLiveData<>();
        cart = new MutableLiveData<>();
    }

    public LiveData<List<CustomerModel>> getAllCustomer(String bearerToken) {
        return customerRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getIsLoadingCustomer() {
        return customerRepository.getIsLoading();
    }

    public LiveData<EmployeeModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getIsLoadingEmployee() {
        return employeeRepository.getIsLoading();
    }

    public void insert(String bearerToken, ProductTransactionModel productTransactionModel) {
        productTransactionRepository.insert(bearerToken, productTransactionModel);
    }

    public LiveData<Boolean> getIsLoading() {
        return productTransactionRepository.getIsLoading();
    }

    public LiveData<Object[]> getIsSuccess() {
        return productTransactionRepository.getIsSuccess();
    }
}
