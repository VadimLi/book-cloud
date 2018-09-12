package com.example.vadim.books_sync.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.vadim.books_sync.model.Folder;

import java.util.List;

@Dao
public interface FolderDao {

    @Insert
    long insert(Folder folder);

    @Update
    void update(Folder folder);

    @Query("DELETE FROM folder")
    void deleteAll();

    @Query("SELECT *FROM folder WHERE name IN (:name)")
    List<Folder> findByName(final String name);

    @Query("DELETE FROM folder WHERE name IN (:name)")
    void deleteByName(final String name);

    @Query("SELECT *FROM folder")
    List<Folder> findAll();

}
