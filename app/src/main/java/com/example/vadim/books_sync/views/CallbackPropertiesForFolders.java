package com.example.vadim.books_sync.views;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.views.customs.TrashAnimationListener;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CallbackPropertiesForFolders implements CallbacksProperties {

    private final PropertiesDialogForFolders propertiesDialogForFolders;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static CallbackPropertiesForFolders.CallbacksEditorImpl newCallbacksEditor(
            PropertiesDialogForFolders propertiesDialogForFolders) {
        return new CallbackPropertiesForFolders(propertiesDialogForFolders)
                .new CallbacksEditorImpl();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private CallbackPropertiesForFolders(
            PropertiesDialogForFolders propertiesDialogForFolders) {
        this.propertiesDialogForFolders = propertiesDialogForFolders;
        if (propertiesDialogForFolders.getContext() instanceof FoldersActivity) {
            propertiesDialogForFolders.renameImageButton.setOnClickListener(onClickRename());
            propertiesDialogForFolders.trashImageButton.setOnClickListener(onClickRemove());
            propertiesDialogForFolders.trashImageButton.setOnLongClickListener(onLongClickRemove());
        }
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
        return v -> {
            final FolderPresenter folderPresenter = propertiesDialogForFolders
                    .getFolderPresenter();
            final String materialName = folderPresenter
                    .getMaterialPresenter()
                    .getName();
            propertiesDialogForFolders.getFolderPresenter().addToFolder(materialName);
            propertiesDialogForFolders.dismiss();
        };
    }

    class CallbacksEditorImpl implements CallbacksProperties.CallbacksEditor {

        @Override
        public View.OnClickListener onClickCancel() {
            return v -> {
                if (propertiesDialogForFolders.getContext() instanceof  FoldersActivity) {
                    propertiesDialogForFolders.folderName.setText(
                            propertiesDialogForFolders.getFolderPresenter().getName());
                    propertiesDialogForFolders.hideEditor();
                } else {
                    propertiesDialogForFolders.dismiss();
                }
            };
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View.OnClickListener onClickApply() {
            return v -> {
                if (propertiesDialogForFolders.getContext() instanceof FoldersActivity) {
                    propertiesDialogForFolders.getFolderPresenter().renameDocument();
                    propertiesDialogForFolders.hideEditor();
                }
            };
        }

        @Override
        public View.OnClickListener onClickEditText() {
            return v -> propertiesDialogForFolders.showKeyBoard();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CallbackPropertiesForFolders create() {
            propertiesDialogForFolders.cancelImageButton.setOnClickListener(onClickCancel());
            propertiesDialogForFolders.folderName.setOnClickListener(onClickEditText());
            if (propertiesDialogForFolders.getContext() instanceof FoldersActivity) {
                propertiesDialogForFolders.applyNameImageButton
                        .setOnClickListener(onClickApply());
            } else {
                propertiesDialogForFolders.applyNameImageButton
                        .setOnClickListener(onClickAddToFile());
            }
            return CallbackPropertiesForFolders.this;
        }

    }

}
