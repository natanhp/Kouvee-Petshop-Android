package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ServiceDetailEndpoint;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.model.ServiceDetailSchema;
import com.p3lj2.koveepetshop.model.ServiceTransactionModel;
import com.p3lj2.koveepetshop.util.RetrofitInstance;
import com.p3lj2.koveepetshop.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailRepository {
    private ServiceDetailEndpoint serviceDetailEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<ServiceDetailComplete>> serviceDetailCompletes = new MutableLiveData<>();
    private static MutableLiveData<Object[]> isSuccess = new MutableLiveData<>();

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

    public void insertTransaction(String bearerToken, ServiceTransactionModel serviceTransactionModel) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        serviceDetailEndpoint.insertTransaction("Bearer " + bearerToken, serviceTransactionModel).enqueue(new Callback<ResponseSchema<ServiceTransactionModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ServiceTransactionModel>> call, @NotNull Response<ResponseSchema<ServiceTransactionModel>> response) {
                if (response.code() == 200 && response.body() != null) {
                    objects[0] = true;
                    objects[1] = response.body().getMessage();
                } else {
                    objects[0] = false;
                    objects[1] = "Transaksi gagal";
                }

                if (response.code() == 400 && response.errorBody() != null) {
                    objects[0] = false;
                    objects[1] = Util.retrofitErrorHandler(response);
                }

                isSuccess.postValue(objects);
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<ServiceTransactionModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = "Transaksi gagal";

                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Object[]> getIsSuccess() {
        return isSuccess;
    }
}
