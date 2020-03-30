package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ServiceDetailEndpoint;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.model.ServiceDetailSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailRepository {
    private ServiceDetailEndpoint serviceDetailEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<ServiceDetailComplete>> serviceDetailCompletes = new MutableLiveData<>();

    public ServiceDetailRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        serviceDetailEndpoint = retrofitInstance.getRetrofit().create(ServiceDetailEndpoint.class);
    }

    public LiveData<List<ServiceDetailComplete>> getAll() {
        isLoading.setValue(true);
        serviceDetailEndpoint.getAll().enqueue(new Callback<ServiceDetailSchema>() {
            @Override
            public void onResponse(@NotNull Call<ServiceDetailSchema> call, @NotNull Response<ServiceDetailSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    serviceDetailCompletes.postValue(response.body().getServiceDetailCompletes());
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ServiceDetailSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return serviceDetailCompletes;
    }

    public void insert(String bearerToken, ServiceDetailModel serviceDetailModel) {
        isLoading.setValue(true);
        serviceDetailEndpoint.insert("Bearer " + bearerToken, serviceDetailModel).enqueue(new Callback<ServiceDetailSchema>() {
            @Override
            public void onResponse(@NotNull Call<ServiceDetailSchema> call, @NotNull Response<ServiceDetailSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ServiceDetailSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void update(String bearerToken, ServiceDetailModel serviceDetailModel) {
        isLoading.setValue(true);
        serviceDetailEndpoint.update("Bearer " + bearerToken, serviceDetailModel).enqueue(new Callback<ServiceDetailSchema>() {
            @Override
            public void onResponse(@NotNull Call<ServiceDetailSchema> call, @NotNull Response<ServiceDetailSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ServiceDetailSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void delete(String bearerToken, int serviceDetailId) {
        isLoading.setValue(true);
        serviceDetailEndpoint.delete("Bearer " + bearerToken, serviceDetailId).enqueue(new Callback<ServiceDetailSchema>() {
            @Override
            public void onResponse(@NotNull Call<ServiceDetailSchema> call, @NotNull Response<ServiceDetailSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ServiceDetailSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
