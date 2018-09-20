package com.example.vadim.books_sync.presenters.states_of_folder;

import android.util.Log;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

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
        return Notifications.REMOVE_FOLDER.getNotification();
    }

}
