package com.lipakov.vadim.booksDocs.presenters.states_of_folder;

import android.util.Log;

import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.model.Folder;
import com.lipakov.vadim.booksDocs.presenters.FolderPresenter;
import com.lipakov.vadim.booksDocs.presenters.Notification;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;

public class RenamingFolder implements StateOfDocument.StateOfFolder {

    private final String folderName;

    private final FolderDao folderDao;

    public RenamingFolder(final String folderName,
                        final FolderDao folderDao) {
        this.folderName = folderName;
        this.folderDao = folderDao;
    }

    @Override
    public void doStateWithFolder(FolderPresenter folderPresenter) {
        final Folder folder = folderPresenter.getFolder();
        folder.setName(folderName);
        folderDao.update(folder);
        folderPresenter.update(folderName);
        folderPresenter.setStateOfFolder(this);
        Log.d("TAG", "Folder has renamed");
    }

    @Override
    public String toString() {
        return Notification.RENAME_FOLDER.getNotification();
    }

}
