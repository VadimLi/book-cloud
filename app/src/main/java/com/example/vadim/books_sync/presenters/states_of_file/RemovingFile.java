package com.example.vadim.books_sync.presenters.states_of_file;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.views.MaterialsOfFolderActivity;
import com.example.vadim.books_sync.views.PropertiesDialogForMaterials;

import java.io.File;

public class RemovingFile implements StateOfDocument.StateOfFile {

    private final MaterialDao materialDao;

    public RemovingFile(final MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final String path = materialPresenter.getPath();
        final File file = new File(path);
        if ( ((PropertiesDialogForMaterials) materialPresenter.getPropertiesDialog()).getContext()
                instanceof MaterialsOfFolderActivity) {
            materialDao.deleteByPath(path);
            materialPresenter.setStateOfFile(this);
        } else if (file.exists()) {
            materialDao.deleteByPath(path);
            file.delete();
            materialPresenter.setStateOfFile(this);
        }
    }

    @Override
    public String toString() {
        return Notifications.REMOVE.getNotification();
    }

}
