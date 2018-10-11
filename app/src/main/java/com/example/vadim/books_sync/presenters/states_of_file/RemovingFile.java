package com.example.vadim.books_sync.presenters.states_of_file;

import android.util.Log;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notification;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.presenters.services.Formats;

import java.io.File;

public class RemovingFile implements StateOfDocument.StateOfFile {

    private final MaterialDao materialDao;

    private final MaterialFolderJoinDao materialFolderJoinDao;

    public RemovingFile(final MaterialDao materialDao,
                        final MaterialFolderJoinDao materialFolderJoinDao) {
        this.materialDao = materialDao;
        this.materialFolderJoinDao = materialFolderJoinDao;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final long materialId = materialPresenter.getId();
        final String path = materialPresenter.getPath();
        final String format = materialPresenter.getFormat();

        final FolderPresenter folderPresenter = materialPresenter.getFolderPresenter();
        final long folderId = folderPresenter.getId();

        if ( Formats.checkNameOfFormat(format) ) {
            final File file = new File(path);
            if ( file.delete() ) {
                materialDao.deleteByPath(path);
                Log.d("TAG", "File has removed");
            }
        } else {
            materialFolderJoinDao.deleteMaterialsByMaterialIdAndFolderId(
                    materialId, folderId);
            Log.d("TAG", "File has removed");
        }
        materialPresenter.setStateOfFile(this);
    }

    @Override
    public String toString() {
        return Notification.REMOVE_FILE.getNotification();
    }

}
