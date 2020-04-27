package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ProductRestockEndpoint;
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
    private static MutableLiveData<List<ProductRestockModel>> restockModels = new MutableLiveData<>();

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

    public LiveData<List<ProductRestockModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        productRestockEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<ResponseSchema<ProductRestockModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductRestockModel>> call, @NotNull Response<ResponseSchema<ProductRestockModel>> response) {
                if (response.body() != null && response.code() == 200) {
                    restockModels.postValue(response.body().getData());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<ProductRestockModel>> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return restockModels;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void confirm(String bearerToken, String productId, int ownerId) {
        isLoading.setValue(true);
        productRestockEndpoint.confirm("Bearer " + bearerToken, productId, ownerId).enqueue(new Callback<ResponseSchema<ProductRestockModel>>() {
            @Override
            public void onResponse(Call<ResponseSchema<ProductRestockModel>> call, Response<ResponseSchema<ProductRestockModel>> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<ResponseSchema<ProductRestockModel>> call, Throwable t) {
                isLoading.postValue(false);
            }
        });
    }
}
