package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.PetSizeEndpoint;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.model.PetSizeSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetSizeRepository {
    private PetSizeEndpoint petSizeEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<PetSizeModel>> petSizeModels = new MutableLiveData<>();

    public PetSizeRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        petSizeEndpoint = retrofitInstance.getRetrofit().create(PetSizeEndpoint.class);
    }

    public LiveData<List<PetSizeModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        petSizeEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<PetSizeSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetSizeSchema> call, @NotNull Response<PetSizeSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    petSizeModels.postValue(response.body().getPetSizeModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetSizeSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return petSizeModels;
    }

    public void insert(String bearerToken, PetSizeModel petSizeModel) {
        isLoading.setValue(true);
        petSizeEndpoint.insert("Bearer " + bearerToken, petSizeModel).enqueue(new Callback<PetSizeSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetSizeSchema> call, @NotNull Response<PetSizeSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetSizeSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
