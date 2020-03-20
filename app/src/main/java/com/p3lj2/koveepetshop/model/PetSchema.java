package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PetSchema {

    @Expose
    private String message;

    @Expose
    @SerializedName("data")
    private List<PetComplete> petCompletes;
}