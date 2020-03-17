package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PetTypeSchema implements Parcelable {
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<PetTypeModel> petTypeModels;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeList(this.petTypeModels);
    }

    protected PetTypeSchema(Parcel in) {
        this.message = in.readString();
        this.petTypeModels = new ArrayList<PetTypeModel>();
        in.readList(this.petTypeModels, PetTypeModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<PetTypeSchema> CREATOR = new Parcelable.Creator<PetTypeSchema>() {
        @Override
        public PetTypeSchema createFromParcel(Parcel source) {
            return new PetTypeSchema(source);
        }

        @Override
        public PetTypeSchema[] newArray(int size) {
            return new PetTypeSchema[size];
        }
    };
}
