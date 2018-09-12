package com.example.vadim.books_sync.presenters.states_of_folder;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

public class AddingNewFolder implements StateOfDocument.StateOfFolder {

    private final FolderDao folderDao;

    public AddingNewFolder(final FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @Override
    public void doStateWithFolder(FolderPresenter folderPresenter) {
        long id = folderDao.insert(folderPresenter.getFolder());
        final Folder newFolder = folderPresenter.getFolder();
        newFolder.setId(id);
        folderPresenter.setStateOfFolder(this);
    }

    @Override
    public String toString() {
        return Notifications.ADD_NEW_FOLDER.getNotification();
    }

}
