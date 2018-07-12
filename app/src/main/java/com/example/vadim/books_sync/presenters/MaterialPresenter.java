package com.example.vadim.books_sync.presenters;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

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

    private void rotateImage() {
        RotateAnimation anim =
                new RotateAnimation(0f,
                        360f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(500);
    }

}
