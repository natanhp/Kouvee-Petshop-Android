package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.CustomerEndpoint;
import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.CustomerSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository {
    private CustomerEndpoint customerEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<CustomerModel>> customerModels = new MutableLiveData<>();

    public CustomerRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        customerEndpoint = retrofitInstance.getRetrofit().create(CustomerEndpoint.class);
    }

    public LiveData<List<CustomerModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        customerEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<CustomerSchema>() {
            @Override
            public void onResponse(@NotNull Call<CustomerSchema> call, @NotNull Response<CustomerSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    customerModels.postValue(response.body().getCustomerModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<CustomerSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return customerModels;
    }

    public void insert(String bearerToken, CustomerModel customerModel) {
        isLoading.setValue(true);
        customerEndpoint.insert("Bearer " + bearerToken, customerModel).enqueue(new Callback<CustomerSchema>() {
            @Override
            public void onResponse(@NotNull Call<CustomerSchema> call, @NotNull Response<CustomerSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<CustomerSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void update(String bearerToken, CustomerModel customerModel) {
        isLoading.setValue(true);
        customerEndpoint.update("Bearer " + bearerToken, customerModel).enqueue(new Callback<CustomerSchema>() {
            @Override
            public void onResponse(@NotNull Call<CustomerSchema> call, @NotNull Response<CustomerSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<CustomerSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void delete(String bearerToken, int customerId, int csId) {
        isLoading.setValue(true);
        customerEndpoint.delete("Bearer " + bearerToken, customerId, csId).enqueue(new Callback<CustomerSchema>() {
            @Override
            public void onResponse(@NotNull Call<CustomerSchema> call, @NotNull Response<CustomerSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<CustomerSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<CustomerModel>> search(String bearerToken, String customerName) {
        isLoading.setValue(true);
        customerEndpoint.search("Bearer " + bearerToken, customerName).enqueue(new Callback<CustomerSchema>() {
            @Override
            public void onResponse(@NotNull Call<CustomerSchema> call, @NotNull Response<CustomerSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    customerModels.postValue(response.body().getCustomerModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<CustomerSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return customerModels;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
