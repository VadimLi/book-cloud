package com.example.vadim.books_sync.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

public class CallbackPropertiesForInsertFolderImpl implements CallbacksProperties.CallbacksEditor {

    private AddFolderDialog addFolderDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    CallbackPropertiesForInsertFolderImpl(AddFolderDialog addFolderDialog) {
        this.addFolderDialog = addFolderDialog;
        addFolderDialog.folderName.setOnClickListener(onClickEditText());
        addFolderDialog.applyName.setOnClickListener(onClickApply());
        addFolderDialog.cancelImageButton.setOnClickListener(onClickCancel());
    }

    @Override
    public View.OnClickListener onClickCancel() {
        return v -> {
            addFolderDialog.hideEditor();
            addFolderDialog.hideKeyBoard();
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnClickListener onClickApply() {
        return v -> {
            final String folderName = String.valueOf(addFolderDialog.folderName.getText());
            addFolderDialog.getFolderPresenter().addToFolderOrNewFolder(folderName);
            addFolderDialog.hideEditor();
            addFolderDialog.hideKeyBoard();
        };
    }

    @Override
    public View.OnClickListener onClickEditText() {
        return v -> {};
    }

}
