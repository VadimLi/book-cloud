package com.example.vadim.books_sync.presenters;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.viewPresenters.MaterialsViewPresenter;
import com.example.vadim.books_sync.presenters.services.FinderService;

import java.util.LinkedList;

import javax.inject.Inject;

public class MaterialPresenter implements MaterialsViewPresenter {

    private FinderService finderService;

    private MaterialsViewPresenter materialsViewPresenter;

    @Inject
    public MaterialPresenter(FinderService finderService) {
        this.finderService = finderService;
    }

    public void attachView(MaterialsViewPresenter materialsViewPresenter) {
        this.materialsViewPresenter = materialsViewPresenter;
    }

    public void detachView() {
        materialsViewPresenter = null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void updateMaterials(LinkedList<Material> materials) {
        final LinkedList<Material> newMaterials = finderService.getMaterials();
        newMaterials.forEach(materials::addFirst);
        finderService.deleteMaterialFiles(materials);
        materialsViewPresenter.updateMaterials(materials);
    }

}
