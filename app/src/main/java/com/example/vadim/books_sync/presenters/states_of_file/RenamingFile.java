package com.example.vadim.books_sync.presenters.states_of_file;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

import java.io.File;

public class RenamingFile implements StateOfDocument.StateOfFile {

    private final String fileName;

    private final MaterialDao materialDao;

    public RenamingFile(final String fileName,
                        final MaterialDao materialDao) {
        this.fileName = fileName;
        this.materialDao = materialDao;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final Material material = materialPresenter.getMaterial();
        final File file = new File(materialPresenter.getPath());
        if (file.exists()) {
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

    @Override
    public String toString() {
        return Notifications.RENAME.getNotification();
    }

}
