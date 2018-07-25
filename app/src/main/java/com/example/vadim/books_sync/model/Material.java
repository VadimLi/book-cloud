package com.example.vadim.books_sync.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;

import javax.inject.Named;


@Entity(indices = {@Index(value = "path",
        unique = true)})
public class Material {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @Named("Хесоям Александрович 1995 годя рождения оформляет дарственную на имя карла джонсона проживающего по адресу грув стрит рядом с нигерами 250 000 долларов новую машину и лекарства всех " +
            "болезней" +
            "")
    private String name;

    @Named("ин")
    private String format;

    @Named("пут")
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

    // НА майнкрафт КС ГОУ ДОТА
}
