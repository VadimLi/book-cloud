package com.example.vadim.books_sync.presenters.states_of_file;

import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.model.MaterialFolderJoin;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notification;
import com.example.vadim.books_sync.presenters.StateOfDocument;

public class AddingFileToFolder implements StateOfDocument.StateOfFile {

    private static final String DELIMITER = " ";

    private static final String QUOTE = "\"";

    private final MaterialFolderJoinDao materialFolderJoinDao;

    private String notification;

    public AddingFileToFolder(final MaterialFolderJoinDao materialFolderJoinDao) {
        this.materialFolderJoinDao = materialFolderJoinDao;
    }

    @Override
    public void doStateWithFile(final MaterialPresenter materialPresenter) {
        final long materialId = materialPresenter.getId();
        final FolderPresenter folderPresenter = materialPresenter.getFolderPresenter();
        final long folderId = folderPresenter.getId();

        final MaterialFolderJoin materialByFolderAndMaterial = materialFolderJoinDao
                .findMaterialsForMaterialAndFolder(materialId, folderId);
        if (materialByFolderAndMaterial == null) {
            final MaterialFolderJoin materialFolderJoin = new MaterialFolderJoin();
            materialFolderJoin.setMaterialId(materialId);
            materialFolderJoin.setFolderId(folderId);
            materialFolderJoinDao.insert(materialFolderJoin);
            setNotification(Notification.ADD_TO_FOLDER, folderPresenter);
        } else {
            setNotification(Notification.FILE_EXISTS, folderPresenter);
        }
        materialPresenter.setStateOfFile(this);
    }

    private String addQuotes(final String folderName) {
        return QUOTE.concat(folderName)
                    .concat(QUOTE);
    }

    private void setNotification(final Notification notification,
                                 final FolderPresenter folderPresenter) {
        this.notification = notification.getNotification().concat(DELIMITER)
                .concat(addQuotes(folderPresenter.getName()));
    }

    @Override
    public String toString() {
        return notification;
    }

}
