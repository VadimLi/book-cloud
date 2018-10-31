package com.lipakov.vadim.booksDocs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(indices = {@Index(value = "name",
        unique = true)})
public class Folder implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    public Folder() {}

    public Folder(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        public Folder[] newArray(int size) {
            return new Folder[size];
        }

    };

}
