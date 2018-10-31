package com.lipakov.vadim.booksDocs.presenters.states_of_folder;

import android.util.Log;

import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.presenters.FolderPresenter;
import com.lipakov.vadim.booksDocs.presenters.Notification;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;

public class RemovingFolder implements StateOfDocument.StateOfFolder {

    private final FolderDao folderDao;

    public RemovingFolder(final FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @Override
    public void doStateWithFolder(FolderPresenter folderPresenter) {
        final String name = folderPresenter.getName();
        folderDao.deleteByName(name);
        folderPresenter.setStateOfFolder(this);
        Log.d("TAG", "Folder has removed");
    }

    @Override
    public String toString() {
        return Notification.REMOVE_FOLDER.getNotification();
    }

}
