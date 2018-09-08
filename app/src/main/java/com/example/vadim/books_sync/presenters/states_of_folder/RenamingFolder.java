package com.example.vadim.books_sync.presenters.states_of_folder;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

public class RenamingFolder implements StateOfDocument.StateOfFolder {

    private final String fileName;

    private final FolderDao folderDao;

    public RenamingFolder(final String fileName,
                        final FolderDao folderDao) {
        this.fileName = fileName;
        this.folderDao = folderDao;
    }

    @Override
    public void doStateWithFolder(FolderPresenter folderPresenter) {
        final Folder folder = folderPresenter.getFolder();
        folder.setName(fileName);
        folderPresenter.update(fileName);
        folderDao.update(folder);
        folderPresenter.setStateOfFolder(this);
    }

    @Override
    public String toString() {
        return Notifications.RENAME.getNotification();
    }

}
