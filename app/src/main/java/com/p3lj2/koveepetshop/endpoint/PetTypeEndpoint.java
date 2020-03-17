package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.model.PetTypeSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PetTypeEndpoint {
    @GET("pettypes/getall")
    Call<PetTypeSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("pettypes/insert")
    Call<PetTypeSchema> insert(@Header("Authorization") String bearerToken, @Body PetTypeModel petTypeModel);

    @PUT("pettypes/update")
    Call<PetTypeSchema> update(@Header("Authorization") String bearerToken, @Body PetTypeModel petTypeModel);
}
