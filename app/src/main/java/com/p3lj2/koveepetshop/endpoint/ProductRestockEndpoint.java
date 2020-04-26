package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ProductRestockEndpoint {
    @POST("productrestock/insert")
    Call<ResponseSchema<ProductRestockModel>> insert(@Header("Authorization") String bearerToken, @Body ProductRestockModel productRestockModel);
}
