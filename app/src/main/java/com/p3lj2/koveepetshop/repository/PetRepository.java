package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.PetEndpoint;
import com.p3lj2.koveepetshop.model.PetComplete;
import com.p3lj2.koveepetshop.model.PetModel;
import com.p3lj2.koveepetshop.model.PetSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRepository {
    private PetEndpoint petEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<PetComplete>> petCompletes = new MutableLiveData<>();

    public PetRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        this.petEndpoint = retrofitInstance.getRetrofit().create(PetEndpoint.class);
    }

    public LiveData<List<PetComplete>> getAll(String bearerToken) {
        isLoading.setValue(true);
        petEndpoint.getAll("Bearer " + bearerToken).enqueue(new Callback<PetSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetSchema> call, @NotNull Response<PetSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    petCompletes.postValue(response.body().getPetCompletes());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return petCompletes;
    }

    public void insert(String bearerToken, PetModel petModel) {
        isLoading.setValue(true);
        petEndpoint.insert("Bearer " + bearerToken, petModel).enqueue(new Callback<PetSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetSchema> call, @NotNull Response<PetSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void update(String bearerToken, PetModel petModel) {
        isLoading.setValue(true);
        petEndpoint.update("Bearer " + bearerToken, petModel).enqueue(new Callback<PetSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetSchema> call, @NotNull Response<PetSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public void delete(String bearerToken, int petId, int csId) {
        isLoading.setValue(true);
        petEndpoint.delete("Bearer " + bearerToken, petId, csId).enqueue(new Callback<PetSchema>() {
            @Override
            public void onResponse(@NotNull Call<PetSchema> call, @NotNull Response<PetSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<PetSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return  isLoading;
    }
}
