package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class PetTypeModel {

    @Expose
    private int id;

    @Expose
    private String type;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int deletedBy;
}
