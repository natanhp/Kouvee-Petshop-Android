package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.CustomerSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerEndpoint {

    @GET("customers/getall")
    Call<CustomerSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("customers/insert")
    Call<CustomerSchema> insert(@Header("Authorization") String bearerToken, @Body CustomerModel customerModel);

    @PUT("customers/update")
    Call<CustomerSchema> update(@Header("Authorization") String bearerToken, @Body CustomerModel customerModel);

    @DELETE("customers/delete/{id}/{csId}")
    Call<CustomerSchema> delete(@Header("Authorization") String bearerToken, @Path("id") int customerId, @Path("csId") int csId);
}
