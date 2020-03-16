package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetTypeSchema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PetTypeEndpoint {
    @GET("pettypes/getall")
    Call<PetTypeSchema> getAll(@Header("Authorization") String bearerToken);
}
