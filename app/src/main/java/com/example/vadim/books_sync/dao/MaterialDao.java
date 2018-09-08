package com.example.vadim.books_sync.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.vadim.books_sync.model.Material;

import java.util.List;

@Dao
public interface MaterialDao {

    @Query("SELECT *FROM material ORDER BY id DESC")
    List<Material> findAll();

    @Query("SELECT *FROM material WHERE path = :path")
    List<Material> findByPath(String path);

    @Query("SELECT *FROM material WHERE format = :format")
    List<Material> findByFormat(String format);

    @Query("DELETE FROM material WHERE path IN (:path)")
    void deleteByPath(String path);

    @Query("SELECT DISTINCT format FROM material")
    List<String> findDistinctName();

    @Query("SELECT DISTINCT format FROM material WHERE name IN (:name)")
    List<String> findDistinctNameByName(final String name);

    @Query("DELETE FROM material WHERE id IN (:id)")
    void deleteById(long id);

    @Query("DELETE FROM material")
    void deleteAll();

    @Update
    void update(Material material);

    @Insert
    void insert(Material material);

}
