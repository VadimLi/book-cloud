package com.example.vadim.books_sync.views;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.vadim.books_sync.presenters.StateOfDocument;

public interface DialogView {
    void hideEditor();
    void showEditor();
    void drawPropertiesDialog(View view);
    void hideKeyBoard();
    void showKeyBoard();
    void showToast(StateOfDocument stateOfDocument);
    void validateOfNameDocument();
}
