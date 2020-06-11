package com.p3lj2.koveepetshop.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.LogEndpoint;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.util.RetrofitInstance;
import com.p3lj2.koveepetshop.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LogRepository {
    private LogEndpoint logEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<Object[]> isSuccess = new MutableLiveData<>();
    private static MutableLiveData<List<ProductModel>> productModels = new MutableLiveData<>();
    private static MutableLiveData<List<SupplierModel>> supplierModels = new MutableLiveData<>();
    private static MutableLiveData<List<EmployeeModel>> employeeModels = new MutableLiveData<>();
    private final String TAG = getClass().getCanonicalName();

    public LogRepository() {
        Retrofit retrofitInstance = RetrofitInstance.getRetrofitInstance().getRetrofit();
        logEndpoint = retrofitInstance.create(LogEndpoint.class);
    }

    public LiveData<List<ProductModel>> getProduct(String bearerToken) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        logEndpoint.getProduct("Bearer " + bearerToken).enqueue(new Callback<ResponseSchema<ProductModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductModel>> call, @NotNull Response<ResponseSchema<ProductModel>> response) {
                if (response.code() == 200 && response.body() != null) {
                    productModels.postValue(response.body().getData());
                    objects[0] = true;
                    objects[1] = response.body().getMessage();
                    Log.i(TAG, response.body().getMessage());
                } else {
                    objects[0] = false;
                    objects[1] = somethingIsWrongMsg;
                    Log.e(TAG, somethingIsWrongMsg);
                }

                if (response.code() == 400 && response.errorBody() != null) {
                    objects[0] = false;
                    String errorMsg = Util.retrofitErrorHandler(response);
                    objects[1] = errorMsg;

                    Log.e(TAG, errorMsg);
                }

                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<ProductModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });

        return productModels;
    }

    public LiveData<List<SupplierModel>> getSupplier(String bearerToken) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        logEndpoint.getSupplier("Bearer " + bearerToken).enqueue(new Callback<ResponseSchema<SupplierModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<SupplierModel>> call, @NotNull Response<ResponseSchema<SupplierModel>> response) {
                if (response.code() == 200 && response.body() != null) {
                    supplierModels.postValue(response.body().getData());
                    objects[0] = true;
                    objects[1] = response.body().getMessage();
                    Log.i(TAG, response.body().getMessage());
                } else {
                    objects[0] = false;
                    objects[1] = somethingIsWrongMsg;
                    Log.e(TAG, somethingIsWrongMsg);
                }

                if (response.code() == 400 && response.errorBody() != null) {
                    objects[0] = false;
                    String errorMsg = Util.retrofitErrorHandler(response);
                    objects[1] = errorMsg;

                    Log.e(TAG, errorMsg);
                }

                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<SupplierModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });

        return supplierModels;
    }

    public LiveData<List<EmployeeModel>> logEmployee(String bearerToken) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        logEndpoint.logEmployee("Bearer " + bearerToken).enqueue(new Callback<ResponseSchema<EmployeeModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<EmployeeModel>> call, @NotNull Response<ResponseSchema<EmployeeModel>> response) {
                if (response.code() == 200 && response.body() != null) {
                    employeeModels.postValue(response.body().getData());
                    objects[0] = true;
                    objects[1] = response.body().getMessage();
                    Log.i(TAG, response.body().getMessage());
                } else {
                    objects[0] = false;
                    objects[1] = somethingIsWrongMsg;
                    Log.e(TAG, somethingIsWrongMsg);
                }

                if (response.code() == 400 && response.errorBody() != null) {
                    objects[0] = false;
                    String errorMsg = Util.retrofitErrorHandler(response);
                    objects[1] = errorMsg;

                    Log.e(TAG, errorMsg);
                }

                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<EmployeeModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });

        return employeeModels;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Object[]> getIsSuccess() {
        return isSuccess;
    }
}
