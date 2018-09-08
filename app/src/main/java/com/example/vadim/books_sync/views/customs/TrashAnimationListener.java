package com.example.vadim.books_sync.views.customs;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;

import com.example.vadim.books_sync.presenters.StateOwnerProperties;
import com.example.vadim.books_sync.views.PropertiesDialogForMaterials;

@SuppressLint("ValidFragment")
public class TrashAnimationListener extends Animation
        implements Animation.AnimationListener {

    private final StateOwnerProperties stateOwnerProperties;

    public TrashAnimationListener(final StateOwnerProperties stateOwnerProperties) {
        this.stateOwnerProperties = stateOwnerProperties;
        setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAnimationEnd(Animation animation) {
        stateOwnerProperties.dismiss();
        stateOwnerProperties.removeDocument();
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }

}
