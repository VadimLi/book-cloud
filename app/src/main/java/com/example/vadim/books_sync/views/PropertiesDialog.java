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
import com.example.vadim.books_sync.presenters.StateOwnerProperties;
import com.example.vadim.books_sync.presenters.states_of_document.Removing;
import com.example.vadim.books_sync.presenters.states_of_document.Renaming;
import com.example.vadim.books_sync.presenters.states_of_document.Sharing;
import com.example.vadim.books_sync.presenters.states_of_document.State;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

@SuppressLint("ValidFragment")
public class PropertiesDialog extends android.support.v4.app.DialogFragment
        implements StateOwnerProperties, DialogView {

    @BindView(R.id.fileName)
    CustomEditText fileNameEditText;

    @BindView(R.id.applyName)
    ImageButton applyNameImageButton;

    @BindView(R.id.cancelImageButton)
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

    private InputMethodManager inputMethodManager;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewProperties = inflater.inflate(R.layout.dialog_properties, null);
        final Context context = viewProperties.getContext();
        ButterKnife.bind(this, viewProperties);
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        Objects.requireNonNull(getActivity())
                                .getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectDialogFragment(this);
        materialPresenter.attachDialog(this);

        new CallbackPropertiesImpl(this);
        new CallbackPropertiesImpl.CallbacksEditorImpl(this);

        drawPropertiesDialog(viewProperties);
        fileNameEditText.setText(materialPresenter.getName());
        hideEditorOfName();
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
        materialPresenter.detachDialog();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        materialPresenter.detachDialog();
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
    public void hideKeyBoard(InputMethodManager inputMethodManager, View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void showKeyBoard(InputMethodManager inputMethodManager) {
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(fileNameEditText,
                    InputMethodManager.SHOW_FORCED);
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void removeDocument() {
        final Removing removing = new Removing(materialDao);
        removing.doState(materialPresenter);
        showToast(materialPresenter.getState());
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void renameDocument() {
        if (fileNameEditText.getText().length() == 0) {
            fileNameEditText.setText(materialPresenter.getName());
        } else {
            final String newNameMaterial = fileNameEditText.getText().toString();
            final String fullName =String.valueOf(getFullNameFile(newNameMaterial));
            fileNameEditText.setText(fullName);
            final Renaming renaming = new Renaming(fullName);
            renaming.doState(materialPresenter);
            showToast(materialPresenter.getState());
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void shareDocument() {
        final Sharing sharing = new Sharing();
        sharing.doState(materialPresenter);
    }

    @Override
    public void addToFolderDocument() {}

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

    String getNameWithoutFormat(MaterialPresenter materialPresenter) {
        String nameMaterial = materialPresenter.getName();
        String format = materialPresenter.getFormat();
        final int lengthName = nameMaterial.length() - (format.length() + 1);
        return nameMaterial.substring(0, lengthName);
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    public void showToast(State state) {
        if (state != null) {
            final Toast toast = Toast.makeText(getActivity(),
                    state.toString(),
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
