package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.CustomerSchema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CustomerEndpoint {

    @GET("customers/getall")
    Call<CustomerSchema> getAll(@Header("Authorization") String bearerToken);
}
