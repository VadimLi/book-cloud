package com.example.vadim.books_sync.presenters.states_of_folder;

import android.util.Log;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

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
        return Notifications.RENAME_FOLDER.getNotification();
    }

}
