package com.example.vadim.books_sync.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.model.MaterialFolderJoin;

import java.util.List;

@Dao
public interface MaterialFolderJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MaterialFolderJoin materialFolderJoin);

    @Query("SELECT * FROM material INNER JOIN material_folder_join ON " +
            "material.id = material_folder_join.materialId WHERE " +
            "material_folder_join.folderId = :folderId")
    List<Material> findMaterialsForFolders(final long folderId);

    @Query("SELECT * FROM folder INNER JOIN material_folder_join ON " +
            "folder.id = material_folder_join.folderId WHERE " +
            "material_folder_join.materialId = :materialId")
    List<Folder> getFoldersForMaterials(final int materialId);

    @Query("SELECT * FROM material_folder_join WHERE materialId = :materialId AND " +
            "folderId = :folderId")
    MaterialFolderJoin findMaterialsForMaterialAndFolder(final long materialId,
                                                         final long folderId);

}
