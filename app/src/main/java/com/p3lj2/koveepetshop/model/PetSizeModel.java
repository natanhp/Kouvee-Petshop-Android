package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class PetSizeModel implements Parcelable {

    @Expose
    private int id;

    @Expose
    private String size;

    @Expose
    private int createdBy;

    @Expose
    private int updatedBy;

    @Expose
    private int deletedBy;

    public PetSizeModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.size);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.deletedBy);
    }

    protected PetSizeModel(Parcel in) {
        this.id = in.readInt();
        this.size = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
    }

    public static final Parcelable.Creator<PetSizeModel> CREATOR = new Parcelable.Creator<PetSizeModel>() {
        @Override
        public PetSizeModel createFromParcel(Parcel source) {
            return new PetSizeModel(source);
        }

        @Override
        public PetSizeModel[] newArray(int size) {
            return new PetSizeModel[size];
        }
    };
}
