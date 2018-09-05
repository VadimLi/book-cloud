package com.example.vadim.books_sync.presenters.states_of_document;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notifications;

import java.io.File;

public class Renaming implements State {

    private final String fileName;

    public Renaming(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void doState(MaterialPresenter materialPresenter) {
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
                materialPresenter.setState(this);
            }
        }
    }

    @Override
    public String toString() {
        return Notifications.RENAME.getNotification();
    }

}
