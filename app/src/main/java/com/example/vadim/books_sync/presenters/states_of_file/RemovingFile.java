package com.example.vadim.books_sync.presenters.states_of_file;

import android.util.Log;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.Notifications;
import com.example.vadim.books_sync.presenters.StateOfDocument;

import java.io.File;

public class RemovingFile implements StateOfDocument.StateOfFile {

    private final MaterialDao materialDao;

    public RemovingFile(final MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        final String path = materialPresenter.getPath();
        final File file = new File(path);
        if ( file.delete() ) {
            materialDao.deleteByPath(path);
            materialPresenter.setStateOfFile(this);
            Log.d("TAG", "File has removed");
            return;
        }
        Log.d("TAG", "File has not removed");
    }

    @Override
    public String toString() {
        return Notifications.REMOVE_FILE.getNotification();
    }

}
