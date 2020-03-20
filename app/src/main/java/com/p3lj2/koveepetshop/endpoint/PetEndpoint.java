package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetModel;
import com.p3lj2.koveepetshop.model.PetSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PetEndpoint {

    @GET("pets/getall")
    Call<PetSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("pets/insert")
    Call<PetSchema> insert(@Header("Authorization") String bearerToken, @Body PetModel petModel);

    @PUT("pets/update")
    Call<PetSchema> update(@Header("Authorization") String bearerToken, @Body PetModel petModel);

    @DELETE("pets/delete/{id}/{csId}")
    Call<PetSchema> delete(@Header("Authorization") String bearerToken, @Path("id") int petId, @Path("csId") int csId);
}
