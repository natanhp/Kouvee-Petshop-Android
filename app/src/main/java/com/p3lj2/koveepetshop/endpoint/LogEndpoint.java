package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.model.SupplierModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LogEndpoint {
    @GET("log/product")
    Call<ResponseSchema<ProductModel>> getProduct(@Header("Authorization") String bearerToken);

    @GET("log/supplier")
    Call<ResponseSchema<SupplierModel>> getSupplier(@Header("Authorization") String bearerToken);

    @GET("log/employee")
    Call<ResponseSchema<EmployeeModel>> logEmployee(@Header("Authorization") String bearerToken);
}
