package com.example.vadim.books_sync.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.vadim.books_sync.model.Material;

import java.util.List;

@Dao
public interface MaterialDao {

    @Query("SELECT *FROM material ORDER BY id DESC")
    List<Material> findAll();

    @Query("SELECT *FROM material WHERE path = :path")
    List<Material> findAllByPath(String path);

    @Query("DELETE FROM material")
    void deleteAll();

    @Insert
    void insert(Material material);

}
