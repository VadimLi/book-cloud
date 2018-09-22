package com.example.vadim.books_sync.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

public class CallbackPropertiesForAddingFolderImpl
        implements CallbacksProperties.CallbacksEditor {

    private final AddingFolderDialog addingFolderDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static CallbackPropertiesForAddingFolderImpl newCallbacksForAddingFolder(
            AddingFolderDialog addingFolderDialog) {
        return new CallbackPropertiesForAddingFolderImpl(addingFolderDialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private CallbackPropertiesForAddingFolderImpl(AddingFolderDialog addingFolderDialog) {
        this.addingFolderDialog = addingFolderDialog;
        addingFolderDialog.folderName.setOnClickListener(onClickEditText());
        addingFolderDialog.applyName.setOnClickListener(onClickApply());
        addingFolderDialog.cancelImageButton.setOnClickListener(onClickCancel());
    }

    @Override
    public View.OnClickListener onClickCancel() {
        return v -> {
            addingFolderDialog.hideEditor();
            addingFolderDialog.hideKeyBoard();
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnClickListener onClickApply() {
        return v -> {
            final String folderName = String.valueOf(addingFolderDialog.folderName.getText());
            addingFolderDialog.getFolderPresenter().addNewFolder(folderName);
            addingFolderDialog.hideEditor();
            addingFolderDialog.hideKeyBoard();
        };
    }

    @Override
    public View.OnClickListener onClickEditText() {
        return v -> {};
    }

}
