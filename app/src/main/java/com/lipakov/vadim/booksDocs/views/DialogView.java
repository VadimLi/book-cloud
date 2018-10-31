package com.lipakov.vadim.booksDocs.views;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;

public interface DialogView {
    void hideEditor();
    void showEditor();
    void drawPropertiesDialog(View view);
    void hideKeyBoard();
    void showKeyBoard();
    void showToast(StateOfDocument stateOfDocument);
    void validateOfNameDocument();
}
