package com.example.vadim.books_sync.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.presenters.StateOwnerProperties;
import com.example.vadim.books_sync.presenters.states_of_file.RemovingFile;
import com.example.vadim.books_sync.presenters.states_of_file.RenamingFile;
import com.example.vadim.books_sync.presenters.states_of_file.SharingFile;

import java.util.Objects;

import javax.inject.Inject;

import static android.widget.Toast.LENGTH_SHORT;

@SuppressLint("ValidFragment")
public class PropertiesDialogForMaterials extends android.support.v4.app.DialogFragment
        implements StateOwnerProperties, DialogView {

    CustomEditText fileNameEditText;

    ImageButton applyNameImageButton;

    ImageButton cancelImageButton;

    ImageButton renameImageButton;

    ImageButton addToFolderImageButton;

    ImageButton trashImageButton;

    ImageButton shareImageButton;

    @Inject
    MaterialDao materialDao;

    private MaterialPresenter materialPresenter;

    private InputMethodManager inputMethodManager;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View viewProperties;
        if (getContext() instanceof MainActivity) {
            viewProperties =
                    inflater.inflate(R.layout.materials_dialog_properties, null);
            fileNameEditText = viewProperties.findViewById(R.id.fileName);
            applyNameImageButton = viewProperties.findViewById(R.id.applyName);
            cancelImageButton = viewProperties.findViewById(R.id.cancelImageButton);
            renameImageButton = viewProperties.findViewById(R.id.rename);
            addToFolderImageButton = viewProperties.findViewById(R.id.addToFolder);
            trashImageButton = viewProperties.findViewById(R.id.trash);
            shareImageButton = viewProperties.findViewById(R.id.share);
        } else {
            viewProperties =
                    inflater.inflate(R.layout.materials_folder_dialog_properties, null);
            fileNameEditText = viewProperties.findViewById(R.id.fileName);
            addToFolderImageButton = viewProperties.findViewById(R.id.addToFolder);
            trashImageButton = viewProperties.findViewById(R.id.trash);
            shareImageButton = viewProperties.findViewById(R.id.share);
        }

        final Context context = viewProperties.getContext();
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        Objects.requireNonNull(getActivity())
                                .getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectDialogFragmentForMaterials(this);
        materialPresenter.attachDialog(this);

        new CallbackPropertiesForMaterialsImpl(this);
        new CallbackPropertiesForMaterialsImpl.CallbacksEditorImpl(this);

        drawPropertiesDialog(viewProperties);
        fileNameEditText.setText(materialPresenter.getName());
        hideEditor();
        inputMethodManager =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        return viewProperties;
    }

    public void setMaterialPresenter(MaterialPresenter materialPresenter) {
        this.materialPresenter = materialPresenter;
    }

    public MaterialPresenter getMaterialPresenter() {
        return materialPresenter;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        materialPresenter.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        materialPresenter.dismiss();
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
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(getView()).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void showKeyBoard() {
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(fileNameEditText,
                    InputMethodManager.SHOW_FORCED);
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void removeDocument() {
        final RemovingFile removingFile = new RemovingFile(materialDao);
        removingFile.doStateWithFile(materialPresenter);
        showToast(materialPresenter.getStateOfFile());
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void renameDocument() {
        if (fileNameEditText.getText().length() == 0) {
            fileNameEditText.setText(materialPresenter.getName());
        } else {
            final String newNameMaterial = fileNameEditText.getText().toString();
            final String fullName = String.valueOf(getFullNameFile(newNameMaterial));
            fileNameEditText.setText(fullName);
            final RenamingFile renamingFile = new RenamingFile(fullName, materialDao);
            renamingFile.doStateWithFile(materialPresenter);
            showToast(materialPresenter.getStateOfFile());
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void shareDocument() {
        final SharingFile sharingFile = new SharingFile();
        sharingFile.doStateWithFile(materialPresenter);
    }

    @Override
    public void addToFolderOrNewFolder(String name) { }

    @Override
    public void hideEditor() {
        fileNameEditText.setVisibleCloseButton(false);
        fileNameEditText.setEnabled(false);
        if (getContext() instanceof MainActivity) {
            applyNameImageButton.setVisibility(View.INVISIBLE);
            cancelImageButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showEditor() {
        fileNameEditText.setVisibleCloseButton(true);
        fileNameEditText.setEnabled(true);
        if (getContext() instanceof MainActivity) {
            applyNameImageButton.setVisibility(View.VISIBLE);
            cancelImageButton.setVisibility(View.VISIBLE);
        }
    }

    String getNameWithoutFormat(MaterialPresenter materialPresenter) {
        String nameMaterial = materialPresenter.getName();
        String format = materialPresenter.getFormat();
        final int lengthName = nameMaterial.length() - (format.length() + 1);
        return nameMaterial.substring(0, lengthName);
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void showToast(StateOfDocument stateOfFile) {
        if (stateOfFile != null) {
            final Toast toast = Toast.makeText(getActivity(),
                    stateOfFile.toString(),
                    LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

    private StringBuilder getFullNameFile(String name) {
        final String format = materialPresenter.getFormat();
        final String dote = ".";
        return new StringBuilder(name)
                .append(dote)
                .append(format);
    }

    public InputMethodManager getInputMethodManager() {
        return inputMethodManager;
    }

}
