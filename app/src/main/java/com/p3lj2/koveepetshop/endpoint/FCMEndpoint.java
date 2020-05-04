package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.FCMModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FCMEndpoint {

    @POST("fcm/insert")
    Call<ResponseSchema<FCMModel>> insert(@Header("Authorization") String bearerToken, @Body FCMModel fcmModel);

    @DELETE("fcm/delete/{token}")
    Call<ResponseSchema<FCMModel>> delete(@Header("Authorization") String bearerTokoen, @Path("token") String token);
}
