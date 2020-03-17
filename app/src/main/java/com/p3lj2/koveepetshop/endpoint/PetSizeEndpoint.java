package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetSizeSchema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PetSizeEndpoint {
    @GET("petsizes/getall")
    Call<PetSizeSchema> getAll(@Header("Authorization") String bearerToken);
}
