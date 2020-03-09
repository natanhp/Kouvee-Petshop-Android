package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.EmployeeLoginSchema;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EmployeeEndpoint {
    @FormUrlEncoded
    @POST("login")
    Call<EmployeeLoginSchema> login(@Field("username") String username, @Field("password") String password);
}
