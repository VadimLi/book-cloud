package com.example.vadim.books_sync.presenters;


import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.utils.Finder;
import com.example.vadim.books_sync.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MaterialPresenter {

    private MainActivity mainActivity;

    private ArrayList<Material> materials = new ArrayList<>();

    private Finder finder;

    public MaterialPresenter(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        finder = new Finder(materials);
    }

    public void attachView(MainActivity usersActivity) {
        mainActivity = usersActivity;
    }

    public void detachView() {
        mainActivity = null;
    }

    public void addMaterialFiles() {
       List<Material> materialList = finder.findFiles();
       mainActivity.fillData(materialList);
    }

}
