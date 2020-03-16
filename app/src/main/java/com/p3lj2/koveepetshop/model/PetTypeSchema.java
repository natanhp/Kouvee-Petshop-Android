package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PetTypeSchema {
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<PetTypeModel> petTypeModels;
}
