package com.example.vadim.books_sync.presenters;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.services.FinderService;
import com.example.vadim.books_sync.basePresenters.BaseMaterialsPresenter;

import java.util.LinkedList;

import javax.inject.Inject;


public class MaterialsUpdaterPresenter implements BaseMaterialsPresenter {

    private FinderService finderService;

    private BaseMaterialsPresenter baseMaterialsPresenter;

    @Inject
    public MaterialsUpdaterPresenter(FinderService finderService) {
        this.finderService = finderService;
    }

    public void attachView(BaseMaterialsPresenter baseMaterialsPresenter) {
        this.baseMaterialsPresenter = baseMaterialsPresenter;
    }

    public void detachView() {
        baseMaterialsPresenter = null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void updateMaterials(LinkedList<Material> materials) {
        final int minSleep = 200;
        final Thread updaterMaterialsThread = new Thread(() -> {
            final LinkedList<Material> newMaterials = finderService.getMaterials();
            for (Material material : newMaterials) {
                materials.addFirst(material);
            }
            final Thread updaterAdapter = new Thread(() -> {
                try {
                    Thread.sleep(minSleep);
                } catch (final InterruptedException e) {
                    Log.e("TAG ", "error: inner updater adapter");
                }
                baseMaterialsPresenter.updateMaterials(materials);
            });
            updaterAdapter.start();
        });
        updaterMaterialsThread.start();
    }

}
