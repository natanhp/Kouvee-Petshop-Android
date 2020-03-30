package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class PetTypeModel implements Parcelable {

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

    public PetTypeModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.type);
        dest.writeInt(this.createdBy);
        dest.writeInt(this.updatedBy);
        dest.writeInt(this.deletedBy);
    }

    protected PetTypeModel(Parcel in) {
        this.id = in.readInt();
        this.type = in.readString();
        this.createdBy = in.readInt();
        this.updatedBy = in.readInt();
        this.deletedBy = in.readInt();
    }

    public static final Parcelable.Creator<PetTypeModel> CREATOR = new Parcelable.Creator<PetTypeModel>() {
        @Override
        public PetTypeModel createFromParcel(Parcel source) {
            return new PetTypeModel(source);
        }

        @Override
        public PetTypeModel[] newArray(int size) {
            return new PetTypeModel[size];
        }
    };
}
