package com.example.vadim.books_sync.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.views.customs.TrashAnimationListener;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CallbackPropertiesForFoldersImpl implements CallbacksProperties {

    private final PropertiesDialogForFolders propertiesDialogForFolders;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static CallbackPropertiesForFoldersImpl.CallbacksEditorImpl newCallbacksEditorImpl(
            PropertiesDialogForFolders propertiesDialogForFolders) {
        return new CallbackPropertiesForFoldersImpl(propertiesDialogForFolders)
                .new CallbacksEditorImpl();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private CallbackPropertiesForFoldersImpl(
            PropertiesDialogForFolders propertiesDialogForFolders) {
        this.propertiesDialogForFolders = propertiesDialogForFolders;
        propertiesDialogForFolders.renameImageButton.setOnClickListener(onClickRename());
        propertiesDialogForFolders.trashImageButton.setOnClickListener(onClickRemove());
        propertiesDialogForFolders.trashImageButton.setOnLongClickListener(onLongClickRemove());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnClickListener onClickShare() {
        return null;
    }

    @Override
    public View.OnClickListener onClickRemove() {
        return v -> {
            final Animation lowTrashAnimation =
                    AnimationUtils.loadAnimation(v.getContext(), R.anim.low_shake_trash);
            propertiesDialogForFolders.trashImageButton.startAnimation(lowTrashAnimation);
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View.OnLongClickListener onLongClickRemove() {
        return v -> {
            final Animation highTrashAnimation =
                    AnimationUtils.loadAnimation(v.getContext(), R.anim.high_shake_trash);
            highTrashAnimation.setAnimationListener(
                    new TrashAnimationListener(propertiesDialogForFolders));
            propertiesDialogForFolders.trashImageButton.startAnimation(highTrashAnimation);
            propertiesDialogForFolders.getFolderPresenter().removeDocument();
            propertiesDialogForFolders.dismiss();
            return false;
        };
    }

    @Override
    public View.OnClickListener onClickRename() {
        return v -> {
            final String nameWithoutFormat = propertiesDialogForFolders
                    .getFolderPresenter()
                    .getName();
            propertiesDialogForFolders.folderName.setText(nameWithoutFormat);
            propertiesDialogForFolders.folderName.setSelection(
                    nameWithoutFormat.length());
            propertiesDialogForFolders.showEditor();
            propertiesDialogForFolders.showKeyBoard();
        };
    }

    @Override
    public View.OnClickListener onClickAddToFile() {
        return null;
    }

    class CallbacksEditorImpl implements CallbacksProperties.CallbacksEditor {

        @Override
        public View.OnClickListener onClickCancel() {
            return v -> {
                propertiesDialogForFolders.folderName.setText(
                        propertiesDialogForFolders.getFolderPresenter().getName());
                propertiesDialogForFolders.hideEditor();
            };
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View.OnClickListener onClickApply() {
            return v -> {
                propertiesDialogForFolders.getFolderPresenter().renameDocument();
                propertiesDialogForFolders.hideEditor();
            };
        }

        @Override
        public View.OnClickListener onClickEditText() {
            return v -> propertiesDialogForFolders.showKeyBoard();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CallbackPropertiesForFoldersImpl create() {
            propertiesDialogForFolders.applyNameImageButton.setOnClickListener(onClickApply());
            propertiesDialogForFolders.cancelImageButton.setOnClickListener(onClickCancel());
            propertiesDialogForFolders.folderName.setOnClickListener(onClickEditText());
            return CallbackPropertiesForFoldersImpl.this;
        }

    }

}
