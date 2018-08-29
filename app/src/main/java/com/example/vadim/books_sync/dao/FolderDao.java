package com.example.vadim.books_sync.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.vadim.books_sync.model.Folder;

import java.util.List;

@Dao
public interface FolderDao {

    @Query("SELECT *FROM folder ORDER BY id DESC")
    List<Folder> findAll();

}
