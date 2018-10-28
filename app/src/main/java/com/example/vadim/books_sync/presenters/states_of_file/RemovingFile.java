package com.example.vadim.books_sync.presenters.states_of_file;

import android.app.Activity;
import android.util.Log;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notification;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.views.MaterialsOfFolderActivity;

import java.io.File;

public class RemovingFile implements StateOfDocument.StateOfFile {

    private final MaterialDao materialDao;

    private final MaterialFolderJoinDao materialFolderJoinDao;

    private final Activity materialsActivity;

    public RemovingFile(final MaterialDao materialDao,
                        final MaterialFolderJoinDao materialFolderJoinDao,
                        final Activity materialsActivity) {
        this.materialDao = materialDao;
        this.materialFolderJoinDao = materialFolderJoinDao;
        this.materialsActivity = materialsActivity;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final long materialId = materialPresenter.getId();
        final String path = materialPresenter.getPath();
        final FolderPresenter folderPresenter = materialPresenter.getFolderPresenter();
        if ( materialsActivity instanceof MaterialsOfFolderActivity ) {
            final long folderId = folderPresenter.getId();
            materialFolderJoinDao.deleteMaterialsByMaterialIdAndFolderId(
                    materialId, folderId);
            Log.d("TAG", "File has removed");
        } else {
            final File file = new File(path);
            if ( file.delete() ) {
                materialDao.deleteByPath(path);
                Log.d("TAG", "File has removed");
            }
        }
        materialPresenter.setStateOfFile(this);
    }

    @Override
    public String toString() {
        return Notification.REMOVE_FILE.getNotification();
    }

}
