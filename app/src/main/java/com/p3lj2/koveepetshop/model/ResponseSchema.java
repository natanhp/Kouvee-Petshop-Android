package com.p3lj2.koveepetshop.model;

import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.Data;

@Data
public class ResponseSchema<T> {

    @Expose(serialize = false)
    private String message;

    @Expose
    private List<T> data;
}
