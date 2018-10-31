package com.lipakov.vadim.booksDocs.presenters.states_of_file;

import com.lipakov.vadim.booksDocs.dao.MaterialDao;
import com.lipakov.vadim.booksDocs.model.Material;
import com.lipakov.vadim.booksDocs.presenters.MaterialPresenter;
import com.lipakov.vadim.booksDocs.presenters.Notification;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;
import com.lipakov.vadim.booksDocs.presenters.services.Formats;

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
        final File file = new File(materialPresenter.getPath());
        final Material material = materialPresenter.getMaterial();

        final String format = material.getFormat();
        if ( !Formats.notCheckNameOfFormat(format) ) {
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
