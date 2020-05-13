package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ProductTransactionEndpoint;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;
import com.p3lj2.koveepetshop.util.Util;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductTransactionRepository {
    private ProductTransactionEndpoint productTransactionEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<Object[]> isSuccess = new MutableLiveData<>();

    public ProductTransactionRepository() {
        Retrofit retrofitInstance = RetrofitInstance.getRetrofitInstance().getRetrofit();
        productTransactionEndpoint = retrofitInstance.create(ProductTransactionEndpoint.class);
    }

    public void insert(String bearerToken, ProductTransactionModel productTransactionModel) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        productTransactionEndpoint.insert("Bearer " + bearerToken, productTransactionModel).enqueue(new Callback<ResponseSchema<ProductTransactionModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductTransactionModel>> call, @NotNull Response<ResponseSchema<ProductTransactionModel>> response) {
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
            public void onFailure(@NotNull Call<ResponseSchema<ProductTransactionModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = "Transaksi gagal";

                isLoading.postValue(false);
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
