package com.example.vadim.books_sync.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
@Entity
public class Folder {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    public Folder() {}

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

}
