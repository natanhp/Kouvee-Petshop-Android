package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.CustomerSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface CustomerEndpoint {

    @GET("customers/getall")
    Call<CustomerSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("customers/insert")
    Call<CustomerSchema> insert(@Header("Authorization") String bearerToken, @Body CustomerModel customerModel);

    @PUT("customers/update")
    Call<CustomerSchema> update(@Header("Authorization") String bearerToken, @Body CustomerModel customerModel);
}
