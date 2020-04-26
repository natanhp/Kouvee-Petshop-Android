package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ProductRestockEndpoint;
import com.p3lj2.koveepetshop.model.ProductRestockDetail;
import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRestockRepository {
    private ProductRestockEndpoint productRestockEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<ProductRestockDetail>> restockModels = new MutableLiveData<>();

    public ProductRestockRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        productRestockEndpoint = retrofitInstance.getRetrofit().create(ProductRestockEndpoint.class);
    }

    public void insert(String bearerToken, ProductRestockModel productRestockModel) {
        isLoading.setValue(true);
        productRestockEndpoint.insert("Bearer " + bearerToken, productRestockModel).enqueue(new Callback<ResponseSchema<ProductRestockModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductRestockModel>> call, @NotNull Response<ResponseSchema<ProductRestockModel>> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<ProductRestockModel>> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
