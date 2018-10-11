package com.example.vadim.books_sync.presenters.states_of_file;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notification;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.presenters.services.Formats;

import java.io.File;

public class RenamingFile implements StateOfDocument.StateOfFile {

    private final String fileName;

    private final MaterialDao materialDao;

    private final MaterialFolderJoinDao materialFolderJoinDao;

    public RenamingFile(final String fileName,
                        final MaterialDao materialDao,
                        final MaterialFolderJoinDao materialFolderJoinDao) {
        this.fileName = fileName;
        this.materialDao = materialDao;
        this.materialFolderJoinDao = materialFolderJoinDao;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final File file = new File(materialPresenter.getPath());
        final Material material = materialPresenter.getMaterial();

        final String format = material.getFormat();
        if ( Formats.checkNameOfFormat(format) ) {
            if ( file.exists() ) {
                final String absolutePath = file.getPath();
                final String filePath =
                        String.valueOf(new StringBuilder(absolutePath.
                                substring(0, absolutePath.lastIndexOf(File.separator)))
                                .append("/")
                                .append(fileName));
                final File newFile = new File(filePath);
                if ( !newFile.exists() ) {
                    file.renameTo(newFile);
                    material.setName(fileName);
                    material.setPath(filePath);
                    materialPresenter.update(fileName);
                    materialDao.update(material);
                    materialPresenter.setStateOfFile(this);
                }
            }
        }
    }

    @Override
    public String toString() {
        return Notification.RENAME_FILE.getNotification();
    }

}
