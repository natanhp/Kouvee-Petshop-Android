package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.repository.ProductRepository;

import java.util.List;

public class ProductTransactionViewModel extends AndroidViewModel {
    private ProductRepository productRepository;

    public ProductTransactionViewModel(@NonNull Application application) {
        super(application);

        productRepository = new ProductRepository();
    }

    public LiveData<List<ProductResponseModel>> getAllProducts() {
        return productRepository.getAll();
    }

    public LiveData<Boolean> getProductIsLoading() {
        return productRepository.getIsLoading();
    }
}
