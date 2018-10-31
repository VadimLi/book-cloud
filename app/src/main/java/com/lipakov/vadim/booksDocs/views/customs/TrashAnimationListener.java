package com.lipakov.vadim.booksDocs.views.customs;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;

import com.lipakov.vadim.booksDocs.presenters.StateOwnerProperties;
import com.lipakov.vadim.booksDocs.views.PropertiesDialogForMaterials;

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
