package com.example.vadim.books_sync.presenters.states_of_file;

import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.model.MaterialFolderJoin;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

public class AddingFileToFolder implements StateOfDocument.StateOfFile {

    private final MaterialFolderJoinDao materialFolderJoinDao;

    public AddingFileToFolder(final MaterialFolderJoinDao materialFolderJoinDao) {
        this.materialFolderJoinDao = materialFolderJoinDao;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final long materialId = materialPresenter.getId();
        final long folderId = materialPresenter.getFolderPresenter().getId();
        final MaterialFolderJoin materialFolderJoin = new MaterialFolderJoin();
        materialFolderJoin.setMaterialId(materialId);
        materialFolderJoin.setFolderId(folderId);
        materialFolderJoinDao.insert(materialFolderJoin);
    }

    @Override
    public String toString() {
        return Notifications.ADD_TO_FOLDER.getNotification();
    }

}
