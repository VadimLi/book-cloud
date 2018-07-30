package com.example.vadim.books_sync.presenters;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.services.FinderService;
import com.example.vadim.books_sync.viewPresenters.MaterialsView;

import java.util.LinkedList;

import javax.inject.Inject;

public class MaterialsUpdaterPresenter implements MaterialsView {

    private FinderService finderService;

    private MaterialsView materialsView;

    @Inject
    public MaterialsUpdaterPresenter(FinderService finderService) {
        this.finderService = finderService;
    }

    public void attachView(MaterialsView materialsView) {
        this.materialsView = materialsView;
    }

    public void detachView() {
        materialsView = null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void updateMaterials(LinkedList<Material> materials) {
        final LinkedList<Material> newMaterials = finderService.getMaterials();
        for (Material material : newMaterials) {
            materials.addFirst(material);
        }
        finderService.deleteMaterialFiles(materials);
        materialsView.updateMaterials(materials);
    }

}
