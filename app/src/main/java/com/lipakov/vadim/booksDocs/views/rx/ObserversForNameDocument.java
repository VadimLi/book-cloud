package com.lipakov.vadim.booksDocs.views.rx;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lipakov.vadim.booksDocs.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObserversForNameDocument {

    public static Observer getNameDocumentObserver(final ImageButton applyName,
                                                   final EditText folderName) {
        return new Observer<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) { }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onNext(Boolean checkName) {
                applyName.setEnabled(checkName);
                if (checkName) {
                    folderName.setTextColor(R.color.colorSuccess);
                } else {
                    folderName.setTextColor(Color.RED);
                }
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }

        };
    }

}
