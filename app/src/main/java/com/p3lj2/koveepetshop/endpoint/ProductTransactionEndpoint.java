package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductTransactionEndpoint {
    @POST("producttransaction/cs/insert")
    Call<ResponseSchema<ProductTransactionModel>> insert(@Header("Authorization") String bearerToken, @Body ProductTransactionModel productTransactionModel);

    @GET("producttransaction/kasir/getall")
    Call<ResponseSchema<ProductTransactionModel>> getAll(@Header("Authorization") String bearerToken);

    @PUT("producttransaction/kasir/updatedetailbyid")
    Call<ResponseSchema<ProductTransactionDetailModel>> updateDetailById(@Header("Authorization") String bearerToken, @Body ProductTransactionDetailModel productTransactionDetailModel);

    @DELETE("producttransaction/kasir/deletedetailbyid/{id}/{cashierId}")
    Call<ResponseSchema<ProductTransactionDetailModel>> deleteDetailById(@Header("Authorization") String bearerToken, @Path("id") int detailId, @Path("cashierId") int cashierId);

    @DELETE("producttransaction/kasir/deletetransactionbyid/{id}/{cashierId}")
    Call<ResponseSchema<ProductTransactionModel>> deleteTransactionById(@Header("Authorization") String bearerToken, @Path("id") String transactionId, @Path("cashierId") int cashierId);
}
