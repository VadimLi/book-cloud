package com.lipakov.vadim.booksDocs.presenters;

public interface StateOfDocument {

    interface StateOfFolder extends StateOfDocument {
        void doStateWithFolder(FolderPresenter folderPresenter);
    }

    interface StateOfFile extends StateOfDocument {
        void doStateWithFile(MaterialPresenter materialPresenter);
    }

}
