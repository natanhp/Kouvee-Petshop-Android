package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.model.ServiceDetailSchema;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServiceDetailEndpoint {
    @GET("noa/servicedetails/getall")
    Call<ServiceDetailSchema> getAll();

    @POST("servicedetails/insert")
    Call<ServiceDetailSchema> insert(@Header("Authorization") String bearerToken, @Body ServiceDetailModel serviceDetailModel);
}
