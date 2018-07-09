package com.example.vadim.books_sync.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;


@Entity(indices = {@Index(value = "path",
        unique = true)})
public class Material {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String format;

    private String path;

    @Ignore
    private Drawable imageUrl;

    public Material() {}

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

}
