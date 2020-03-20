package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class PetComplete implements Parcelable {

    @Expose
    private PetModel pet;

    @Expose
    private String type;

    @Expose
    private String size;

    @Expose
    private String customer;

    public PetComplete() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.pet, flags);
        dest.writeString(this.type);
        dest.writeString(this.size);
        dest.writeString(this.customer);
    }

    protected PetComplete(Parcel in) {
        this.pet = in.readParcelable(PetModel.class.getClassLoader());
        this.type = in.readString();
        this.size = in.readString();
        this.customer = in.readString();
    }

    public static final Parcelable.Creator<PetComplete> CREATOR = new Parcelable.Creator<PetComplete>() {
        @Override
        public PetComplete createFromParcel(Parcel source) {
            return new PetComplete(source);
        }

        @Override
        public PetComplete[] newArray(int size) {
            return new PetComplete[size];
        }
    };
}
