package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.model.PetSizeSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PetSizeEndpoint {
    @GET("uni/petsizes/getall")
    Call<PetSizeSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("petsizes/insert")
    Call<PetSizeSchema> insert(@Header("Authorization") String bearerToken, @Body PetSizeModel petSizeModel);

    @PUT("petsizes/update")
    Call<PetSizeSchema> update(@Header("Authorization") String bearerToken, @Body PetSizeModel petSizeModel);

    @DELETE("petsizes/delete/{id}/{ownerId}")
    Call<PetSizeSchema> delete(@Header("Authorization") String bearerToken, @Path("id") int petSizeId, @Path("ownerId") int ownerId);

    @GET("uni/petsizes/getbysize/{size}")
    Call<PetSizeSchema> search(@Header("Authorization") String bearerToken, @Path("size") String petSize);
}
