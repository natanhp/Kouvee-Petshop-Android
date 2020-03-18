package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ServiceEndpoint;
import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.model.ServiceSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {
    private ServiceEndpoint serviceEndpoint;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<ServiceModel>> serviceModels = new MutableLiveData<>();

    public ServiceRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        serviceEndpoint = retrofitInstance.getRetrofit().create(ServiceEndpoint.class);
    }

    public LiveData<List<ServiceModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        serviceEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<ServiceSchema>() {
            @Override
            public void onResponse(@NotNull Call<ServiceSchema> call, @NotNull Response<ServiceSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    serviceModels.postValue(response.body().getServiceModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ServiceSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return serviceModels;
    }

    public void insert(String bearerToken, ServiceModel serviceModel) {
        isLoading.setValue(true);
        serviceEndpoint.insert("Bearer " + bearerToken, serviceModel).enqueue(new Callback<ServiceSchema>() {
            @Override
            public void onResponse(@NotNull Call<ServiceSchema> call, @NotNull Response<ServiceSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ServiceSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
