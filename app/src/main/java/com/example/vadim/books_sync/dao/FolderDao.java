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
    void insertRootFolder(Folder folder);

    @Query("DELETE FROM folder WHERE root IS 1")
    void deleteAllByRoot();

    @Query("SELECT *FROM folder WHERE root IS 1")
    List<Folder> findByRoot();

    @Query("SELECT *FROM folder")
    List<Folder> findAll();

}
