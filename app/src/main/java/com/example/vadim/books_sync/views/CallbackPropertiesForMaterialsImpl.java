package com.example.vadim.books_sync.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.views.customs.TrashAnimationListener;

public class CallbackPropertiesForMaterialsImpl implements CallbacksProperties {

    private final PropertiesDialogForMaterials propertiesDialogForMaterials;

    @RequiresApi(api = Build.VERSION_CODES.M)
    CallbackPropertiesForMaterialsImpl(PropertiesDialogForMaterials propertiesDialogForMaterials) {
        if (propertiesDialogForMaterials.getContext() instanceof MainActivity) {
            propertiesDialogForMaterials.renameImageButton.setOnClickListener(onClickRename());
        }
        this.propertiesDialogForMaterials = propertiesDialogForMaterials;
        propertiesDialogForMaterials.shareImageButton.setOnClickListener(onClickShare());
        propertiesDialogForMaterials.trashImageButton.setOnClickListener(onClickRemove());
        propertiesDialogForMaterials.trashImageButton.setOnLongClickListener(onLongClickRemove());
        propertiesDialogForMaterials.addToFolderImageButton.setOnClickListener(onClickAddToFile());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnClickListener onClickShare() {
        return v -> propertiesDialogForMaterials.getMaterialPresenter().shareDocument();
    }

    @Override
    public View.OnClickListener onClickRemove() {
        return v -> {
            final Animation lowTrashAnimation =
                    AnimationUtils.loadAnimation(v.getContext(), R.anim.low_shake_trash);
            propertiesDialogForMaterials.trashImageButton.startAnimation(lowTrashAnimation);
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnLongClickListener onLongClickRemove() {
        return v -> {
            final Animation highTrashAnimation =
                    AnimationUtils.loadAnimation(v.getContext(), R.anim.high_shake_trash);
            highTrashAnimation.setAnimationListener(
                    new TrashAnimationListener(propertiesDialogForMaterials));
            propertiesDialogForMaterials.trashImageButton.startAnimation(highTrashAnimation);
            propertiesDialogForMaterials.getMaterialPresenter().removeDocument();
            propertiesDialogForMaterials.dismiss();
            return false;
        };
    }

    @Override
    public View.OnClickListener onClickRename() {
        return v -> {
            String nameWithoutFormat = propertiesDialogForMaterials.getNameWithoutFormat(
                    propertiesDialogForMaterials.getMaterialPresenter());
            propertiesDialogForMaterials.fileNameEditText.setText(nameWithoutFormat);
            propertiesDialogForMaterials.fileNameEditText.setSelection(nameWithoutFormat.length());
            propertiesDialogForMaterials.showEditor();
            propertiesDialogForMaterials.showKeyBoard();
        };
    }

    @Override
    public View.OnClickListener onClickAddToFile() {
        return null;
    }

    static class CallbacksEditorImpl implements CallbacksProperties.CallbacksEditor {

        private PropertiesDialogForMaterials propertiesDialogForMaterials;

        private InputMethodManager inputMethodManager;

        @RequiresApi(api = Build.VERSION_CODES.M)
        CallbacksEditorImpl(PropertiesDialogForMaterials propertiesDialogForMaterials) {
            if (propertiesDialogForMaterials.getContext() instanceof MainActivity) {
                this.propertiesDialogForMaterials = propertiesDialogForMaterials;
                inputMethodManager = propertiesDialogForMaterials.getInputMethodManager();
                propertiesDialogForMaterials.applyNameImageButton.setOnClickListener(onClickApply());
                propertiesDialogForMaterials.cancelImageButton.setOnClickListener(onClickCancel());
                propertiesDialogForMaterials.fileNameEditText.setOnClickListener(onClickEditText());
            }
        }

        @Override
        public View.OnClickListener onClickCancel() {
            return v -> {
                propertiesDialogForMaterials.fileNameEditText.setText(
                        propertiesDialogForMaterials.getMaterialPresenter().getName());
                propertiesDialogForMaterials.hideEditor();
            };
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View.OnClickListener onClickApply() {
            return v -> {
                propertiesDialogForMaterials.getMaterialPresenter().renameDocument();
                propertiesDialogForMaterials.hideEditor();
            };
        }

        @Override
        public View.OnClickListener onClickEditText() {
            return v -> propertiesDialogForMaterials.showKeyBoard();
        }

    }

}
