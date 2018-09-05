package com.example.vadim.books_sync.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.views.customs.TrashAnimationListener;

public class CallbackPropertiesImpl implements CallbacksProperties {

    private final PropertiesDialog propertiesDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    CallbackPropertiesImpl(PropertiesDialog propertiesDialog) {
        this.propertiesDialog = propertiesDialog;
        propertiesDialog.shareImageButton.setOnClickListener(onClickShare());
        propertiesDialog.trashImageButton.setOnClickListener(onClickRemove());
        propertiesDialog.trashImageButton.setOnLongClickListener(onLongClickRemove());
        propertiesDialog.renameImageButton.setOnClickListener(onClickRename());
        propertiesDialog.addToFolderImageButton.setOnClickListener(onClickAddToFile());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnClickListener onClickShare() {
        return v -> propertiesDialog.getMaterialPresenter().shareDocument();
    }

    @Override
    public View.OnClickListener onClickRemove() {
        return v -> {
            final Animation lowTrashAnimation =
                    AnimationUtils.loadAnimation(v.getContext(), R.anim.low_shake_trash);
            propertiesDialog.trashImageButton.startAnimation(lowTrashAnimation);
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnLongClickListener onLongClickRemove() {
        return v -> {
            final Animation highTrashAnimation =
                    AnimationUtils.loadAnimation(v.getContext(), R.anim.high_shake_trash);
            highTrashAnimation.setAnimationListener(
                    new TrashAnimationListener(propertiesDialog));
            propertiesDialog.trashImageButton.startAnimation(highTrashAnimation);
            propertiesDialog.getMaterialPresenter().removeDocument();
            propertiesDialog.dismiss();
            return false;
        };
    }

    @Override
    public View.OnClickListener onClickRename() {
        return v -> {
            String nameWithoutFormat = propertiesDialog.getNameWithoutFormat(
                    propertiesDialog.getMaterialPresenter());
            propertiesDialog.fileNameEditText.setText(nameWithoutFormat);
            propertiesDialog.fileNameEditText.setSelection(nameWithoutFormat.length());
            propertiesDialog.showEditorOfName();
            propertiesDialog.showKeyBoard(propertiesDialog.getInputMethodManager());
        };
    }

    @Override
    public View.OnClickListener onClickAddToFile() {
        return null;
    }

    static class CallbacksEditorImpl implements CallbacksProperties.CallbacksEditor {

        private final PropertiesDialog propertiesDialog;

        private InputMethodManager inputMethodManager;

        @RequiresApi(api = Build.VERSION_CODES.M)
        CallbacksEditorImpl(PropertiesDialog propertiesDialog) {
            this.propertiesDialog = propertiesDialog;
            inputMethodManager = propertiesDialog.getInputMethodManager();
            propertiesDialog.applyNameImageButton.setOnClickListener(onClickApply());
            propertiesDialog.cancelImageButton.setOnClickListener(onClickCancel());
            propertiesDialog.fileNameEditText.setOnClickListener(onClickEditText());
        }

        @Override
        public View.OnClickListener onClickCancel() {
            return v -> {
                propertiesDialog.fileNameEditText.setText(
                        propertiesDialog.getMaterialPresenter().getName());
                propertiesDialog.hideEditorOfName();
            };
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View.OnClickListener onClickApply() {
            return v -> {
                propertiesDialog.getMaterialPresenter().renameDocument();
                propertiesDialog.hideEditorOfName();
            };
        }

        @Override
        public View.OnClickListener onClickEditText() {
            return v -> propertiesDialog.showKeyBoard(inputMethodManager);
        }

    }

}
