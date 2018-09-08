package com.example.vadim.books_sync.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.presenters.StateOwnerProperties;
import com.example.vadim.books_sync.presenters.states_of_folder.AddingNewFolder;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFolderDialog extends android.support.v4.app.DialogFragment
        implements DialogView, StateOwnerProperties {

    @BindView(R.id.folderName)
    CustomEditText folderName;

    @BindView(R.id.cancelImageButton)
    ImageButton cancelImageButton;

    @BindView(R.id.applyName)
    ImageButton applyName;

    @Inject
    FolderDao folderDao;

    private InputMethodManager inputMethodManager;

    private FolderPresenter folderPresenter;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewProperties =
                inflater.inflate(R.layout.add_new_folder_alert_dialog, null);
        final Context context = viewProperties.getContext();
        inputMethodManager =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        ButterKnife.bind(this, viewProperties);
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        Objects.requireNonNull(getActivity())
                                .getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectAddFolderDialog(this);
        folderPresenter.attachDialog(this);

        new CallbackPropertiesForInsertFolderImpl(this);

        drawPropertiesDialog(viewProperties);
        showKeyBoard();

        return viewProperties;
    }

    @Override
    public void hideEditor() {
        dismiss();
        folderPresenter.dismiss();
    }

    @Override
    public void showEditor() {

    }

    @Override
    public void drawPropertiesDialog(View viewProperties) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        viewProperties.setBackground(ContextCompat.getDrawable(
                viewProperties.getContext(), R.drawable.properties_dialog_bg));
    }

    @Override
    public void hideKeyBoard() {
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void showKeyBoard() {
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void showToast(StateOfDocument stateOfDocument) {

    }

    public void setFolderPresenter(FolderPresenter folderPresenter) {
        this.folderPresenter = folderPresenter;
    }

    public FolderPresenter getFolderPresenter() {
        return folderPresenter;
    }

    @Override
    public void removeDocument() {

    }

    @Override
    public void renameDocument() {

    }

    @Override
    public void shareDocument() {

    }

    @Override
    public void addToFolderOrNewFolder(final String name) {
        final AddingNewFolder addingNewFolder = new AddingNewFolder(folderDao);
        addingNewFolder.doStateWithFolder(folderPresenter);
        showToast(folderPresenter.getStateOfFolder());
    }

}
