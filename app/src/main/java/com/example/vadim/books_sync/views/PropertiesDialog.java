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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class PropertiesDialog extends DialogFragment {

    @BindView(R.id.fileName)
    CustomEditText fileNameEditText;

    @BindView(R.id.applyName)
    ImageButton editNameImageButton;

    @BindView(R.id.rename)
    ImageButton renameImageButton;

    @BindView(R.id.addToFolder)
    ImageButton addToFolderImageButton;

    @BindView(R.id.trash)
    ImageButton trashImageButton;

    @BindView(R.id.share)
    ImageButton shareImageButton;

    private MaterialPresenter materialPresenter;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewProperties = inflater.inflate(R.layout.dialog_properties, null);
        ButterKnife.bind(this, viewProperties);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        viewProperties.setBackground(ContextCompat.getDrawable(
                viewProperties.getContext(), R.drawable.properties_dialog_bg));
        final InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        final String nameMaterial = materialPresenter.getName();
        final String format = materialPresenter.getFormat();
        final int lengthName = nameMaterial.length() - (format.length() + 1);
        final String nameWithoutFormat = nameMaterial.substring(0, lengthName);

        fileNameEditText.setText(nameMaterial);
        fileNameEditText.setEnabled(false);

        editNameImageButton.setVisibility(View.INVISIBLE);

        fileNameEditText.setVisibleCloseButton(false);
        fileNameEditText.setOnClickListener(v -> {
            if (imm != null) {
                imm.showSoftInput(fileNameEditText, InputMethodManager.SHOW_FORCED);
            }
        });

        fileNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fileNameEditText.handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editNameImageButton.setOnClickListener(v -> {
            Log.d("state ", "click image button");
        });

        renameImageButton.setOnClickListener(v -> {
            fileNameEditText.setEnabled(true);
            fileNameEditText.requestFocus();
            fileNameEditText.setFocusableInTouchMode(true);
            fileNameEditText.setVisibleCloseButton(true);
            fileNameEditText.setText(nameWithoutFormat);
            editNameImageButton.setVisibility(View.VISIBLE);
            fileNameEditText.setSelection(nameWithoutFormat.length());
            if (imm != null) {
                imm.showSoftInput(fileNameEditText, InputMethodManager.SHOW_FORCED);
            }
        });

        editNameImageButton.setOnClickListener(v -> {
            // update text of edit text
            fileNameEditText.setEnabled(false);
            editNameImageButton.setVisibility(View.INVISIBLE);
        });

        addToFolderImageButton.setOnClickListener(v ->
                Log.d("state ", "add to folder"));

        final Animation highTrashAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.high_shake_trash);
        final Animation lowTrashAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.low_shake_trash);

        highTrashAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
                materialPresenter.removeDocument();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        trashImageButton.setOnClickListener(v ->
                trashImageButton.startAnimation(lowTrashAnimation));

        trashImageButton.setOnLongClickListener(v -> {
            trashImageButton.startAnimation(highTrashAnimation);
            File file = new File(materialPresenter.getPath());
            if (file.exists()) {
                file.delete();
            }
            return true;
        });

        shareImageButton.setOnClickListener(v -> {
            File file = new File(materialPresenter.getPath());
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

}
