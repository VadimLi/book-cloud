package com.example.vadim.books_sync.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


@Entity(indices = {@Index(value = "path",
        unique = true)})
public class Material implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String format;

    private String path;

    public Material() {}

    public Material(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.format = in.readString();
        this.path = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.format);
        dest.writeString(this.path);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Material createFromParcel(Parcel in) {
            return new Material(in);
        }

        public Material[] newArray(int size) {
            return new Material[size];
        }

    };
}
