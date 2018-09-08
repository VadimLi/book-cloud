package com.example.vadim.books_sync.presenters;

public interface StateOfDocument {

    interface StateOfFolder extends StateOfDocument {
        void doStateWithFolder(FolderPresenter folderPresenter);
    }

    interface StateOfFile extends StateOfDocument {
        void doStateWithFile(MaterialPresenter materialPresenter);
    }

}
