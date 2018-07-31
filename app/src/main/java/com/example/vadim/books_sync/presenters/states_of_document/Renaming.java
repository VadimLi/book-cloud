package com.example.vadim.books_sync.presenters.states_of_document;

import android.view.View;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.io.File;
import java.io.IOException;

public class Renaming extends AbstractStateProperties {

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
            }
        }
    }

}
