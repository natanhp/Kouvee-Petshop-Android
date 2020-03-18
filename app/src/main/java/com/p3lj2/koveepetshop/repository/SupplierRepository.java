package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.SupplierEndpoint;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.model.SupplierSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierRepository {
    private SupplierEndpoint supplierEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<SupplierModel>> supplierModels = new MutableLiveData<>();

    public SupplierRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        supplierEndpoint = retrofitInstance.getRetrofit().create(SupplierEndpoint.class);
    }

    public LiveData<List<SupplierModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        supplierEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<SupplierSchema>() {
            @Override
            public void onResponse(@NotNull Call<SupplierSchema> call, @NotNull Response<SupplierSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    supplierModels.postValue(response.body().getSupplierModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<SupplierSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return supplierModels;
    }

    public void insert(String bearerToken, SupplierModel supplierModel) {
        isLoading.setValue(true);
        supplierEndpoint.insert("Bearer " + bearerToken, supplierModel).enqueue(new Callback<SupplierSchema>() {
            @Override
            public void onResponse(@NotNull Call<SupplierSchema> call, @NotNull Response<SupplierSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<SupplierSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void update(String bearerToken, SupplierModel supplierModel) {
        isLoading.setValue(true);
        supplierEndpoint.update("Bearer " + bearerToken, supplierModel).enqueue(new Callback<SupplierSchema>() {
            @Override
            public void onResponse(@NotNull Call<SupplierSchema> call, @NotNull Response<SupplierSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<SupplierSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
