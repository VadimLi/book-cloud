package com.example.vadim.books_sync.views.listeners;


import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

public class ImageButtonAnimation {

    private final ImageButton imageButton;

    private final RotateAnimation rotateAnimation;

    public ImageButtonAnimation(ImageButton imageButton) {
        this.imageButton = imageButton;
        rotateAnimation = new RotateAnimation(
                0f, 360f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
    }

    public void startAnimation() {
        imageButton.clearAnimation();
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(400);
        rotateAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        imageButton.startAnimation(rotateAnimation);
    }

    public RotateAnimation getRotateAnimation() {
        return rotateAnimation;
    }

}
