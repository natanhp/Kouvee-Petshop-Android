package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductTransactionViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private static MutableLiveData<HashMap<Integer, Integer>> viewPositions = new MutableLiveData<>();
    private static MutableLiveData<List<ProductModel>> cart = new MutableLiveData<>();

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
}
