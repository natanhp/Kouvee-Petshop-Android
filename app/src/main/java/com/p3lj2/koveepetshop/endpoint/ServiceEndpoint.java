package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.model.ServiceSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceEndpoint {
    @GET("services/getall")
    Call<ServiceSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("services/insert")
    Call<ServiceSchema> insert(@Header("Authorization") String bearerToken, @Body ServiceModel serviceModel);

    @PUT("services/update")
    Call<ServiceSchema> update(@Header("Authorization") String bearerToken, @Body ServiceModel serviceModel);

    @DELETE("services/delete/{id}/{ownerId}")
    Call<ServiceSchema> delete(@Header("Authorization") String bearerToken, @Path("id") int serviceId, @Path("ownerId") int ownerId);
}
