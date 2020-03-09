package com.p3lj2.koveepetshop.util;

import com.p3lj2.koveepetshop.BuildConfig;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static RetrofitInstance retrofitInstance;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    @Getter
    private Retrofit retrofit;

    private RetrofitInstance() {
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public static RetrofitInstance getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new RetrofitInstance();
        }

        return retrofitInstance;
    }
}
