package com.example.vadim.books_sync.presenters.states_of_folder;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.StateOfDocument;

public class AddingNewFolder implements StateOfDocument.StateOfFolder {

    private final FolderDao folderDao;

    public AddingNewFolder(final FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @Override
    public void doStateWithFolder(FolderPresenter folderPresenter) {
        folderDao.insert(folderPresenter.getFolder());
    }

}
