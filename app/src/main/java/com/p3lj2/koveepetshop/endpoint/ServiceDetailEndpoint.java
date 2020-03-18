package com.p3lj2.koveepetshop.endpoint;

import com.p3lj2.koveepetshop.model.ServiceDetailSchema;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceDetailEndpoint {
    @GET("noa/servicedetails/getall")
    Call<ServiceDetailSchema> getAll();
}
