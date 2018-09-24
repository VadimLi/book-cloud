package com.example.vadim.books_sync.presenters.states_of_folder;

import android.util.Log;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.Notification;
import com.example.vadim.books_sync.presenters.StateOfDocument;

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
