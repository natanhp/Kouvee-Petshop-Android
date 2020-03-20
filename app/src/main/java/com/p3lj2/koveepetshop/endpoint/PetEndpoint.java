package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetSchema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PetEndpoint {

    @GET("pets/getall")
    Call<PetSchema> getAll(@Header("Authorization") String bearerToken);
}
