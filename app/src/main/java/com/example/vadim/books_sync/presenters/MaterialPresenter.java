package com.example.vadim.books_sync.presenters;


import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vadim.books_sync.databinding.ActivityMainBinding;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.utils.Finder;
import com.example.vadim.books_sync.views.MainActivity;

import java.util.List;

import javax.inject.Inject;

public class MaterialPresenter extends BaseObservable {

    private Finder finder;

    private MainActivity mainActivity;

    public ObservableArrayList<String> name;

    public ObservableArrayList<String> format;

    public ObservableArrayList<String> path;

    private List<Material> materials;

    @Inject
    public MaterialPresenter(Finder finder) {
        this.finder = finder;
    }

    public MaterialPresenter() {
        name = new ObservableArrayList<>();
        format = new ObservableArrayList<>();
        path = new ObservableArrayList<>();
    }

    public void attachView(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void detachView() {
        mainActivity = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addMaterialFiles(ActivityMainBinding binding) {
        finder.findFiles();
        mainActivity.showListView(binding);
    }

    public List<Material> getMaterials() {
        return materials;
    }

}
