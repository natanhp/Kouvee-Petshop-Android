package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetModel;
import com.p3lj2.koveepetshop.model.PetSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PetEndpoint {

    @GET("pets/getall")
    Call<PetSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("pets/insert")
    Call<PetSchema> insert(@Header("Authorization") String bearerToken, @Body PetModel petModel);
}
