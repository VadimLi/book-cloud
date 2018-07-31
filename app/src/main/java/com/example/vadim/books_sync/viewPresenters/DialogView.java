package com.example.vadim.books_sync.viewPresenters;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public interface DialogView {
    void hideEditorOfName();
    void showEditorOfName();
    void drawPropertiesDialog(View view);
    void hideKeyBoard(InputMethodManager inputMethodManager, View view);
    void showKeyBoard(InputMethodManager inputMethodManager);
}
