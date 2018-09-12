package com.example.vadim.books_sync.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.vadim.books_sync.model.Material;

import java.util.List;

@Dao
public interface MaterialDao {

    @Query("SELECT *FROM material ORDER BY id DESC")
    List<Material> findAll();

    @Query("SELECT *FROM material WHERE path IN (:path)")
    List<Material> findByPath(String path);

    @Query("SELECT *FROM material WHERE format IN (:format)")
    List<Material> findByFormat(final String format);

    @Query("DELETE FROM material WHERE path IN (:path)")
    void deleteByPath(String path);

    @Query("SELECT DISTINCT format FROM material")
    List<String> findDistinctFormats();

    @Query("SELECT DISTINCT format FROM material WHERE format IN (:format)")
    List<String> findDistinctNameByFormat(final String format);

    @Update
    void update(Material material);

    @Insert
    long insert(Material material);

}
