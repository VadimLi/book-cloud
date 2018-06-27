package com.example.vadim.books_sync.presenters;


import android.app.Application;

import com.example.vadim.books_sync.presenters.utils.Finder;
import com.example.vadim.books_sync.views.MainActivity;

import javax.inject.Inject;

public class MaterialPresenter extends Application {

    private MainActivity mainActivity;

    private Finder finder;

    @Inject
    public MaterialPresenter(Finder finder) {
        this.finder = finder;
    }

    public void attachView(MainActivity usersActivity) {
        mainActivity = usersActivity;
    }

    public void detachView() {
        mainActivity = null;
    }

    public void addMaterialFiles() {
        finder.findFiles();
        mainActivity.showListView();
    }

}
