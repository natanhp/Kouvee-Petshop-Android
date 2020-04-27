package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductRestockEndpoint {
    @POST("productrestock/insert")
    Call<ResponseSchema<ProductRestockModel>> insert(@Header("Authorization") String bearerToken, @Body ProductRestockModel productRestockModel);

    @GET("productrestock/getall")
    Call<ResponseSchema<ProductRestockModel>> getAll(@Header("Authorization") String bearerToken);

    @GET("productrestock/confirm/{id}/{ownerId}")
    Call<ResponseSchema<ProductRestockModel>> confirm(@Header("Authorization") String bearerToken, @Path("id") String productId, @Path("ownerId") int ownerId);
}
