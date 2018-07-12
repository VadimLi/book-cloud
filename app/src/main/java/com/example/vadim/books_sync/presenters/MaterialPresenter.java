package com.example.vadim.books_sync.presenters;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.mvp.MaterialsMvpView;
import com.example.vadim.books_sync.presenters.services.FinderService;

import java.util.LinkedList;

import javax.inject.Inject;

public class MaterialPresenter implements MaterialsMvpView {

    private FinderService finderService;

    private MaterialsMvpView materialsMvpView;

    @Inject
    public MaterialPresenter(FinderService finderService) {
        this.finderService = finderService;
    }

    public void attachView(MaterialsMvpView materialsMvpView) {
        this.materialsMvpView = materialsMvpView;
    }

    public void detachView() {
        materialsMvpView = null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void loadMaterialFiles(LinkedList<Material> materials) {
        final LinkedList<Material> newMaterials = finderService.getMaterials();
        newMaterials.forEach(materials::addFirst);
        finderService.deleteMaterialFiles(materials);
        materialsMvpView.loadMaterialFiles(materials);
    }

}
