package com.example.vadim.books_sync.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "material_folder_join",
        primaryKeys = { "materialId", "folderId" },
        foreignKeys = {
                @ForeignKey(entity = Material.class,
                        parentColumns = "id",
                        childColumns = "materialId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE),
                @ForeignKey(entity = Folder.class,
                        parentColumns = "id",
                        childColumns = "folderId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE)
        })
public class MaterialFolderJoin {

    private long materialId;

    private long folderId;

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

}
