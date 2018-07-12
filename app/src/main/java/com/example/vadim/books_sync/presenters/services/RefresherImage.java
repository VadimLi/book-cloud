package com.example.vadim.books_sync.presenters.services;


import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

public class RefresherImage implements Runnable {

    private final ImageButton scanButton;

    public RefresherImage(ImageButton scanButton) {
        this.scanButton = scanButton;
        final Thread thread = new Thread("rotate image");
        Log.i("thread is createn", String.valueOf(thread));
        thread.start();
    }

    @Override
    public void run() {
        final RotateAnimation anim =
                new RotateAnimation(0f,
                        360f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(500);
        scanButton.startAnimation(anim);
    }

}
