package com.example.vadim.books_sync.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.StateOwnerProperties;
import com.example.vadim.books_sync.presenters.states_of_document.Removing;
import com.example.vadim.books_sync.presenters.states_of_document.Renaming;
import com.example.vadim.books_sync.presenters.states_of_document.Sharing;
import com.example.vadim.books_sync.viewPresenters.DialogView;
import com.example.vadim.books_sync.views.listeners.TrashAnimationListener;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class PropertiesDialog extends DialogFragment
        implements StateOwnerProperties, DialogView {

    @BindView(R.id.fileName)
    CustomEditText fileNameEditText;

    @BindView(R.id.applyName)
    ImageButton applyNameImageButton;

    @BindView(R.id.cancelName)
    ImageButton cancelImageButton;

    @BindView(R.id.rename)
    ImageButton renameImageButton;

    @BindView(R.id.addToFolder)
    ImageButton addToFolderImageButton;

    @BindView(R.id.trash)
    ImageButton trashImageButton;

    @BindView(R.id.share)
    ImageButton shareImageButton;

    @Inject
    MaterialDao materialDao;

    private MaterialPresenter materialPresenter;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewProperties = inflater.inflate(R.layout.dialog_properties, null);
        final Context context = viewProperties.getContext();
        ButterKnife.bind(this, viewProperties);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectDialogFragment(this);
        materialPresenter.attachDialog(this);

        drawPropertiesDialog(viewProperties);
        final String nameMaterial = materialPresenter.getName();
        fileNameEditText.setText(nameMaterial);
        hideEditorOfName();
        final InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        hideKeyBoard(inputMethodManager, viewProperties);
        viewProperties.clearFocus();

        final Animation lowTrashAnimation =
                AnimationUtils.loadAnimation(context, R.anim.low_shake_trash);
        final Animation highTrashAnimation =
                AnimationUtils.loadAnimation(context, R.anim.high_shake_trash);
        highTrashAnimation.setAnimationListener(
                new TrashAnimationListener(this));
        trashImageButton.setOnClickListener(v ->
                trashImageButton.startAnimation(lowTrashAnimation));
        trashImageButton.setOnLongClickListener(v -> {
            trashImageButton.startAnimation(highTrashAnimation);
            removeDocument();
            dismiss();
            return false;
        });

        fileNameEditText.setOnClickListener(v -> {
            showKeyBoard(inputMethodManager);
        });

        shareImageButton.setOnClickListener(v -> {
            shareDocument();
            dismiss();
        });

        addToFolderImageButton.setOnClickListener(v ->
                Log.d("state ", "add to folder"));

        renameImageButton.setOnClickListener(v -> {
            String nameWithoutFormat = getNameWithoutFormat(materialPresenter);
            fileNameEditText.setText(nameWithoutFormat);
            fileNameEditText.setSelection(nameWithoutFormat.length());
            showEditorOfName();
            showKeyBoard(inputMethodManager);
        });

        cancelImageButton.setOnClickListener(v -> {
            fileNameEditText.setText(nameMaterial);
            hideEditorOfName();
            hideKeyBoard(inputMethodManager, viewProperties);
        });

        applyNameImageButton.setOnClickListener(v -> {
            renameDocument();
            hideEditorOfName();
            hideKeyBoard(inputMethodManager, viewProperties);
        });

        return viewProperties;
    }

    public void setMaterialPresenter(MaterialPresenter materialPresenter) {
        this.materialPresenter = materialPresenter;
    }

    public MaterialPresenter getMaterialPresenter() {
        return materialPresenter;
    }

    @Override
    public void drawPropertiesDialog(View viewProperties) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        viewProperties.setBackground(ContextCompat.getDrawable(
                viewProperties.getContext(), R.drawable.properties_dialog_bg));
    }

    @Override
    public void hideKeyBoard(InputMethodManager inputMethodManager, View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showKeyBoard(InputMethodManager inputMethodManager) {
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(fileNameEditText,
                    InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void removeDocument() {
        final Removing removing = new Removing(materialDao);
        materialPresenter.setAbstractPropertiesState(removing);
        materialPresenter.removeDocument();
    }

    @Override
    public void renameDocument() {
        if (fileNameEditText.getText().length() == 0) {
            fileNameEditText.setText(materialPresenter.getName());
        } else {
            final String newNameMaterial = fileNameEditText.getText().toString();
            final String fullName =String.valueOf(getFullNameFile(newNameMaterial));
            fileNameEditText.setText(fullName);
            final Renaming renaming = new Renaming(fullName);
            materialPresenter.setAbstractPropertiesState(renaming);
            materialPresenter.renameDocument();
        }
    }

    @Override
    public void shareDocument() {
        final Sharing sharing = new Sharing();
        materialPresenter.setAbstractPropertiesState(sharing);
        materialPresenter.shareDocument();
    }

    @Override
    public void addToFolderDocument() {

    }

    @Override
    public void hideEditorOfName() {
        fileNameEditText.setVisibleCloseButton(false);
        fileNameEditText.setEnabled(false);
        applyNameImageButton.setVisibility(View.INVISIBLE);
        cancelImageButton.setVisibility(View.INVISIBLE);
        applyNameImageButton.setVisibility(View.INVISIBLE);
        cancelImageButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEditorOfName() {
        fileNameEditText.setVisibleCloseButton(true);
        fileNameEditText.setEnabled(true);
        applyNameImageButton.setVisibility(View.VISIBLE);
        cancelImageButton.setVisibility(View.VISIBLE);
        applyNameImageButton.setVisibility(View.VISIBLE);
        cancelImageButton.setVisibility(View.VISIBLE);
    }

    private String getNameWithoutFormat(MaterialPresenter materialPresenter) {
        String nameMaterial = materialPresenter.getName();
        String format = materialPresenter.getFormat();
        final int lengthName = nameMaterial.length() - (format.length() + 1);
        return nameMaterial.substring(0, lengthName);
    }

    private StringBuilder getFullNameFile(String name) {
        final String format = materialPresenter.getFormat();
        final String dote = ".";
        return new StringBuilder(name)
                .append(dote)
                .append(format);
    }

}
