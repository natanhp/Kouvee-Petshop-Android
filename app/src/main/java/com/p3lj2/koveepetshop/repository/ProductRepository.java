package com.p3lj2.koveepetshop.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.endpoint.ProductEndpoint;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.model.ProductSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ProductEndpoint productEndpoint;
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static MutableLiveData<List<ProductResponseModel>> productResponseLiveData = new MutableLiveData<>();

    public ProductRepository() {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        productEndpoint = retrofitInstance.getRetrofit().create(ProductEndpoint.class);
    }

    public void insert(String token, ProductResponseModel productResponseModel) {
        isLoading.setValue(true);
        File imageFile = new File(productResponseModel.getImageUrl());
        RequestBody requestImage = RequestBody.create(imageFile, MediaType.parse("multipart/form-data"));

        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestImage);
        RequestBody productName = RequestBody.create(productResponseModel.getProductModel().getProductName(), MediaType.parse("multipart/form-data"));
        RequestBody productQuantity = RequestBody.create(String.valueOf(productResponseModel.getProductModel().getProductQuantity()), MediaType.parse("multipart/form-data"));
        RequestBody productPrice = RequestBody.create(String.valueOf(productResponseModel.getProductModel().getProductPrice()), MediaType.parse("multipart/form-data"));
        RequestBody measurement = RequestBody.create(productResponseModel.getProductModel().getMeassurement(), MediaType.parse("multipart/form-data"));
        RequestBody createdBy = RequestBody.create(String.valueOf(productResponseModel.getProductModel().getCreatedBy()), MediaType.parse("multipart/form-data"));
        RequestBody minimumQty = RequestBody.create(String.valueOf(productResponseModel.getProductModel().getMinimumQty()), MediaType.parse("multipart/form-data"));

        productEndpoint.insert("Bearer " + token, productName, productQuantity, productPrice, measurement, createdBy, image, minimumQty).enqueue(new Callback<ProductSchema>() {
            @Override
            public void onResponse(@NotNull Call<ProductSchema> call, @NotNull Response<ProductSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    System.out.println(response.body().getProductResponseModels().get(0).getProductModel().getProductName());
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ProductSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<ProductResponseModel>> getAll() {
        isLoading.setValue(true);

        productEndpoint.getAll().enqueue(new Callback<ProductSchema>() {
            @Override
            public void onResponse(@NotNull Call<ProductSchema> call, @NotNull Response<ProductSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    productResponseLiveData.postValue(response.body().getProductResponseModels());
                }

                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ProductSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });

        return productResponseLiveData;
    }

    public void delete(String token, int id, int ownerId) {
        isLoading.setValue(true);

        productEndpoint.delete("Bearer " + token, id, ownerId).enqueue(new Callback<ProductSchema>() {
            @Override
            public void onResponse(@NotNull Call<ProductSchema> call, @NotNull Response<ProductSchema> response) {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NotNull Call<ProductSchema> call, @NotNull Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
