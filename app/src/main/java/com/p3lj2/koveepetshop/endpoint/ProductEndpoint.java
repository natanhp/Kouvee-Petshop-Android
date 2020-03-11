package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ProductSchema;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProductEndpoint {
    @Multipart
    @POST("products/insert")
    Call<ProductSchema> insert(@Header("Authorization") String bearerToken, @Part("productName") RequestBody productName,
                               @Part("productQuantity") RequestBody productQuantity, @Part("productPrice") RequestBody productPrice,
                               @Part("meassurement") RequestBody measurement, @Part("createdBy") RequestBody createdBy,
                               @Part MultipartBody.Part image, @Part("minimumQty") RequestBody minimumQty);
}
