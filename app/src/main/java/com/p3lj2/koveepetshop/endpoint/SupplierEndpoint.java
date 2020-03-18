package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.model.SupplierSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SupplierEndpoint {
    @GET("suppliers/getall")
    Call<SupplierSchema> getAll(@Header("Authorization") String bearerToken);

    @POST("suppliers/insert")
    Call<SupplierSchema> insert(@Header("Authorization") String bearerToken, @Body SupplierModel supplierModel);
}
