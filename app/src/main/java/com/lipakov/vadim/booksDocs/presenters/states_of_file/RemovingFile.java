package com.lipakov.vadim.booksDocs.presenters.states_of_file;

import android.app.Activity;
import android.util.Log;

import com.lipakov.vadim.booksDocs.dao.MaterialDao;
import com.lipakov.vadim.booksDocs.dao.MaterialFolderJoinDao;
import com.lipakov.vadim.booksDocs.presenters.FolderPresenter;
import com.lipakov.vadim.booksDocs.presenters.MaterialPresenter;
import com.lipakov.vadim.booksDocs.presenters.Notification;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;
import com.lipakov.vadim.booksDocs.views.MaterialsOfFolderActivity;

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
