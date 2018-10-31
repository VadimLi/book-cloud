package com.lipakov.vadim.booksDocs.presenters.states_of_folder;

import android.util.Log;

import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.model.Folder;
import com.lipakov.vadim.booksDocs.presenters.FolderPresenter;
import com.lipakov.vadim.booksDocs.presenters.Notification;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;

public class AddingNewFolder implements StateOfDocument.StateOfFolder {

    private final FolderDao folderDao;

    public AddingNewFolder(final FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @Override
    public void doStateWithFolder(FolderPresenter folderPresenter) {
        final Folder newFolder = folderPresenter.getFolder();
        long id = folderDao.insert(folderPresenter.getFolder());
        if (id != 0) {
            newFolder.setId(id);
            folderPresenter.setStateOfFolder(this);
            Log.d("TAG", "Folder has added");
            return;
        }
        Log.d("TAG", "Folder has not added");
    }

    @Override
    public String toString() {
        return Notification.ADD_NEW_FOLDER.getNotification();
    }

}
