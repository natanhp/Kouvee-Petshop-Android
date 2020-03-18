package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.SupplierSchema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SupplierEndpoint {
    @GET("suppliers/getall")
    Call<SupplierSchema> getAll(@Header("Authorization") String bearerToken);
}
