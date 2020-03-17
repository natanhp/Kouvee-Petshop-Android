package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.PetTypeEndpoint;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.model.PetTypeSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetTypeRepository {
    private PetTypeEndpoint petTypeEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<PetTypeModel>> petTypeModels = new MutableLiveData<>();

    public PetTypeRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        petTypeEndpoint = retrofitInstance.getRetrofit().create(PetTypeEndpoint.class);
    }

    public LiveData<List<PetTypeModel>> getAll(String bearerToken) {
        isLoading.setValue(true);
        petTypeEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<PetTypeSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetTypeSchema> call, @NotNull Response<PetTypeSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    petTypeModels.postValue(response.body().getPetTypeModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetTypeSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return petTypeModels;
    }

    public void insert(String bearerToken, PetTypeModel petTypeModel) {
        isLoading.setValue(true);
        petTypeEndpoint.insert("Bearer " + bearerToken, petTypeModel).enqueue(new Callback<PetTypeSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetTypeSchema> call, @NotNull Response<PetTypeSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetTypeSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void update(String bearerToken, PetTypeModel petTypeModel) {
        isLoading.setValue(true);
        petTypeEndpoint.update("Bearer " + bearerToken, petTypeModel).enqueue(new Callback<PetTypeSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetTypeSchema> call, @NotNull Response<PetTypeSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetTypeSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void delete(String bearerToken, int petTypeId, int ownerId) {
        isLoading.setValue(true);
        petTypeEndpoint.delete("Bearer " + bearerToken, petTypeId, ownerId).enqueue(new Callback<PetTypeSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetTypeSchema> call, @NotNull Response<PetTypeSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetTypeSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
