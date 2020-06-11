package com.p3lj2.koveepetshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
@Entity(tableName = "employee_table")
public class EmployeeModel implements Parcelable {

    @Expose
    @PrimaryKey
    private int id;

    @Expose
    private String name;

    @Expose
    private String address;

    @Expose
    private String dateBirth;

    @Expose
    private String phoneNumber;

    @Expose
    private String role;

    @Expose
    private String username;

    @Expose
    private String password;

    @Expose(serialize = false)
    private String token;

    @Ignore
    @Expose(serialize = false)
    private EmployeeModel creator;

    @Ignore
    @Expose(serialize = false)
    private EmployeeModel updater;

    @Ignore
    @Expose(serialize = false)
    private EmployeeModel deletor;

    @Ignore
    @Expose(serialize = false)
    private String createdAt;

    @Ignore
    @Expose(serialize = false)
    private String updatedAt;

    @Ignore
    @Expose(serialize = false)
    private String deletedAt;

    public EmployeeModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.dateBirth);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.role);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.token);
        dest.writeParcelable(this.creator, flags);
        dest.writeParcelable(this.updater, flags);
        dest.writeParcelable(this.deletor, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    protected EmployeeModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.dateBirth = in.readString();
        this.phoneNumber = in.readString();
        this.role = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.token = in.readString();
        this.creator = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.updater = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.deletor = in.readParcelable(EmployeeModel.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Creator<EmployeeModel> CREATOR = new Creator<EmployeeModel>() {
        @Override
        public EmployeeModel createFromParcel(Parcel source) {
            return new EmployeeModel(source);
        }

        @Override
        public EmployeeModel[] newArray(int size) {
            return new EmployeeModel[size];
        }
    };
}
