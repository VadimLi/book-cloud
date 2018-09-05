package com.example.vadim.books_sync.views.customs;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAnimationEnd(Animation animation) {
        propertiesDialog.dismiss();
        propertiesDialog.getMaterialPresenter().removeDocument();
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }

}
