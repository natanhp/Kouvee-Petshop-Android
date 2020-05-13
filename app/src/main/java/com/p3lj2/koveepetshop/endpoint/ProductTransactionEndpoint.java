package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ProductTransactionEndpoint {
    @POST("producttransaction/cs/insert")
    Call<ResponseSchema<ProductTransactionModel>> insert(@Header("Authorization") String bearerToken, @Body ProductTransactionModel productTransactionModel);
}
