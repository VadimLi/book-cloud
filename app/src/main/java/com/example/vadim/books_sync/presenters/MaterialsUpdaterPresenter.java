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

    private Thread updaterAdapter;

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
            newMaterials.forEach(materials::addFirst);
            finderService.deleteMaterialFiles(materials);
            updaterAdapter = new Thread(() -> {
                try {
                    Thread.sleep(minSleep);
                    if (baseMaterialsPresenter != null) {
                        baseMaterialsPresenter.updateMaterials(materials);
                    }
                } catch (final InterruptedException e) {
                    Log.e("TAG ", "error: inner updater adapter");
                }
            });
            updaterAdapter.start();
        });
        updaterMaterialsThread.start();
    }

    public Thread getUpdaterAdapter() {
        return updaterAdapter;
    }

    public void setUpdaterAdapter(Thread updaterAdapter) {
        this.updaterAdapter = updaterAdapter;
    }

}
