package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.model.PetSizeSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PetSizeEndpoint {
    @GET("petsizes/getall")
    Call<PetSizeSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("petsizes/insert")
    Call<PetSizeSchema> insert(@Header("Authorization") String bearerToken, @Body PetSizeModel petSizeModel);

    @PUT("petsizes/update")
    Call<PetSizeSchema> update(@Header("Authorization") String bearerToken, @Body PetSizeModel petSizeModel);
}
