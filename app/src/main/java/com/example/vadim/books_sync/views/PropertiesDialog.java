package com.example.vadim.books_sync.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.animations.TrashAnimationListener;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class PropertiesDialog extends DialogFragment {

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

        drawPropertiesDialog(viewProperties);

        final InputMethodManager imm =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        final String nameMaterial = materialPresenter.getName();
        final String nameWithoutFormat = getNameWithoutName(nameMaterial);
        initFileNameEditText(nameMaterial);

        applyNameImageButton.setVisibility(View.INVISIBLE);
        cancelImageButton.setVisibility(View.INVISIBLE);

        fileNameEditText.setOnClickListener(v -> {
            if (imm != null) {
                imm.showSoftInput(fileNameEditText, InputMethodManager.SHOW_FORCED);
            }
        });

        renameImageButton.setOnClickListener(v -> {
            fileNameEditText.setEnabled(true);
            fileNameEditText.requestFocus();
            fileNameEditText.setFocusableInTouchMode(true);
            fileNameEditText.setVisibleCloseButton(true);
            fileNameEditText.setText(nameWithoutFormat);
            applyNameImageButton.setVisibility(View.VISIBLE);
            cancelImageButton.setVisibility(View.VISIBLE);
            fileNameEditText.setSelection(nameWithoutFormat.length());
            if (imm != null) {
                imm.showSoftInput(fileNameEditText, InputMethodManager.SHOW_FORCED);
            }
        });

        applyNameImageButton.setOnClickListener(v -> {
            setInvisibleForEditTextIsFalse();
            final Material material = materialPresenter.getMaterial();
            material.setName(fileNameEditText.getText().toString());
            materialDao.update(material);
        });

        cancelImageButton.setOnClickListener(v -> {
            setInvisibleForEditTextIsFalse();
        });

        addToFolderImageButton.setOnClickListener(v ->
                Log.d("state ", "add to folder"));

        final Animation lowTrashAnimation =
                AnimationUtils.loadAnimation(context, R.anim.low_shake_trash);

        final Animation highTrashAnimation =
                AnimationUtils.loadAnimation(context, R.anim.high_shake_trash);
        highTrashAnimation.setAnimationListener(new TrashAnimationListener(this));

        trashImageButton.setOnClickListener(v ->
                trashImageButton.startAnimation(lowTrashAnimation));

        trashImageButton.setOnLongClickListener(v -> {
            trashImageButton.startAnimation(highTrashAnimation);
            final String path = materialPresenter.getPath();
            final File file = new File(path);
            if (file.exists()) {
                materialDao.deleteByPath(path);
                file.delete();
            }
            return false;
        });

        shareImageButton.setOnClickListener(v -> {
            final File file = new File(materialPresenter.getPath());
            if (file.exists()) {
                final Uri uriToFile = Uri.fromFile(file);
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, nameMaterial));
            }
        });

        return viewProperties;
    }

    public void setMaterialPresenter(MaterialPresenter materialPresenter) {
        this.materialPresenter = materialPresenter;
    }

    public MaterialPresenter getMaterialPresenter() {
        return materialPresenter;
    }

    private void drawPropertiesDialog(View viewProperties) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        viewProperties.setBackground(ContextCompat.getDrawable(
                viewProperties.getContext(), R.drawable.properties_dialog_bg));
    }

    private void setInvisibleForEditTextIsFalse() {
        fileNameEditText.setVisibleCloseButton(false);
        fileNameEditText.setEnabled(false);
        applyNameImageButton.setVisibility(View.INVISIBLE);
        cancelImageButton.setVisibility(View.INVISIBLE);
    }

    private String getNameWithoutName(String nameMaterial) {
        final String format = materialPresenter.getFormat();
        final int lengthName = nameMaterial.length() - (format.length() + 1);
        return nameMaterial.substring(0, lengthName);
    }

    private void initFileNameEditText(String nameMaterial) {
        fileNameEditText.setText(nameMaterial);
        fileNameEditText.setEnabled(false);
        fileNameEditText.setVisibleCloseButton(false);
    }

}
