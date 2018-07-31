package com.example.vadim.books_sync.presenters.states_of_document;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.io.File;

public class Removing extends AbstractStateProperties {

    private final MaterialDao materialDao;

    public Removing(final MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    @Override
    public void doState(MaterialPresenter materialPresenter) {
        final String path = materialPresenter.getPath();
        final File file = new File(path);
        if (file.exists()) {
            materialDao.deleteByPath(path);
            file.delete();
        }
    }

}
