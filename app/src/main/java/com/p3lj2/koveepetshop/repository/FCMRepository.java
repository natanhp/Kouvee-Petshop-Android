package com.p3lj2.koveepetshop.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.FCMEndpoint;
import com.p3lj2.koveepetshop.model.FCMModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class FCMRepository {
    private FCMEndpoint fcmEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<FCMModel>> fcmModels = new MutableLiveData<>();

    public FCMRepository() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance().getRetrofit();
        this.fcmEndpoint = retrofit.create(FCMEndpoint.class);
    }

    public void insert(String bearerToken, FCMModel fcmModel) {
        isLoading.setValue(true);
        fcmEndpoint.insert("Bearer " + bearerToken, fcmModel).enqueue(new Callback<ResponseSchema<FCMModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<FCMModel>> call, @NotNull Response<ResponseSchema<FCMModel>> response) {
                if (response.body() != null && response.code() == 200) {
                    Log.w(TAG, response.body().getMessage());
                } else if (response.errorBody() != null) {
                    Log.w(TAG, response.errorBody().toString());
                } else {
                    Log.w(TAG, "Request error");
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<FCMModel>> call, @NotNull Throwable t) {
                Log.w(TAG, Objects.requireNonNull(t.getMessage()));

                isLoading.postValue(false);
            }
        });
    }

    public void delete(String bearerToken, String token) {
        isLoading.setValue(true);
        fcmEndpoint.delete("Bearer " + bearerToken, token).enqueue(new Callback<ResponseSchema<FCMModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<FCMModel>> call, @NotNull Response<ResponseSchema<FCMModel>> response) {
                if (response.body() != null && response.code() == 200) {
                    Log.i(TAG, response.body().getMessage());
                } else if (response.errorBody() != null) {
                    Log.w(TAG, response.errorBody().toString());
                } else {
                    Log.w(TAG, "Request error");
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<FCMModel>> call, @NotNull Throwable t) {
                Log.w(TAG, Objects.requireNonNull(t.getMessage()));

                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
