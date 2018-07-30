package com.example.vadim.books_sync.views.animations;

import android.annotation.SuppressLint;
import android.view.animation.Animation;

import com.example.vadim.books_sync.views.PropertiesDialog;

@SuppressLint("ValidFragment")
public class TrashAnimationListener extends Animation
        implements Animation.AnimationListener {

    private final PropertiesDialog propertiesDialog;

    public TrashAnimationListener(final PropertiesDialog propertiesDialog) {
        this.propertiesDialog = propertiesDialog;
        setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        propertiesDialog.dismiss();
        propertiesDialog.getMaterialPresenter().removeDocument();
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }


}
