package com.p3lj2.koveepetshop.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ProductTransactionEndpoint;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;
import com.p3lj2.koveepetshop.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductTransactionRepository {
    private ProductTransactionEndpoint productTransactionEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<Object[]> isSuccess = new MutableLiveData<>();
    private static MutableLiveData<List<ProductTransactionModel>> productTransactionModels = new MutableLiveData<>();
    private final String TAG = getClass().getCanonicalName();

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
                isSuccess.postValue(objects);
            }
        });
    }

    public LiveData<List<ProductTransactionModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        productTransactionEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<ResponseSchema<ProductTransactionModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductTransactionModel>> call, @NotNull Response<ResponseSchema<ProductTransactionModel>> response) {
                if (response.code() == 200 && response.body() != null) {
                    productTransactionModels.postValue(response.body().getData());
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
            public void onFailure(@NotNull Call<ResponseSchema<ProductTransactionModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });

        return productTransactionModels;
    }

    public void updateDetailById(String bearerToken, ProductTransactionDetailModel productTransactionDetailModel) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        productTransactionEndpoint.updateDetailById("Bearer " + bearerToken, productTransactionDetailModel).enqueue(new Callback<ResponseSchema<ProductTransactionDetailModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductTransactionDetailModel>> call, @NotNull Response<ResponseSchema<ProductTransactionDetailModel>> response) {
                if (response.code() == 200 && response.body() != null) {
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
            public void onFailure(@NotNull Call<ResponseSchema<ProductTransactionDetailModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });
    }

    public void deleteDetailById(String bearerToken, int detailId, int cashierId) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        productTransactionEndpoint.deleteDetailById("Bearer " + bearerToken, detailId, cashierId).enqueue(new Callback<ResponseSchema<ProductTransactionDetailModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductTransactionDetailModel>> call, @NotNull Response<ResponseSchema<ProductTransactionDetailModel>> response) {
                if (response.code() == 200 && response.body() != null) {
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
            public void onFailure(@NotNull Call<ResponseSchema<ProductTransactionDetailModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
                isLoading.postValue(false);
                isSuccess.postValue(objects);
            }
        });
    }

    public void deleteTransactionById(String bearerToken, String transactionid, int cashierId) {
        isLoading.setValue(true);
        isSuccess = new MutableLiveData<>();
        Object[] objects = new Object[2];
        String somethingIsWrongMsg = "Terjadi Kesalahan";
        productTransactionEndpoint.deleteTransactionById("Bearer " + bearerToken, transactionid, cashierId).enqueue(new Callback<ResponseSchema<ProductTransactionModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<ProductTransactionModel>> call, @NotNull Response<ResponseSchema<ProductTransactionModel>> response) {
                if (response.code() == 200 && response.body() != null) {
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
            public void onFailure(@NotNull Call<ResponseSchema<ProductTransactionModel>> call, @NotNull Throwable t) {
                objects[0] = false;
                objects[1] = somethingIsWrongMsg;
                Log.v(TAG, Objects.requireNonNull(t.getMessage()));
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
