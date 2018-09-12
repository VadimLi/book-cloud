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
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.StateOfDocument;
import com.example.vadim.books_sync.presenters.StateOwnerProperties;
import com.example.vadim.books_sync.presenters.states_of_folder.RemovingFolder;
import com.example.vadim.books_sync.presenters.states_of_folder.RenamingFolder;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class PropertiesDialogForFolders extends android.support.v4.app.DialogFragment
        implements StateOwnerProperties, DialogView {

    @BindView(R.id.folderName)
    CustomEditText folderName;

    @BindView(R.id.applyName)
    ImageButton applyNameImageButton;

    @BindView(R.id.cancelImageButton)
    ImageButton cancelImageButton;

    @BindView(R.id.rename)
    ImageButton renameImageButton;

    @BindView(R.id.trash)
    ImageButton trashImageButton;

    @Inject
    FolderDao folderDao;

    private FolderPresenter folderPresenter;

    private InputMethodManager inputMethodManager;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewProperties =
                inflater.inflate(R.layout.folders_dialog_properties, null);
        final Context context = viewProperties.getContext();
        ButterKnife.bind(this, viewProperties);
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        Objects.requireNonNull(getActivity())
                                .getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectDialogFragmentForFolders(this);
        folderPresenter.attachDialog(this);

        new CallbackPropertiesForFoldersImpl(this);
        new CallbackPropertiesForFoldersImpl.CallbacksEditorImpl(this);

        drawPropertiesDialog(viewProperties);
        folderName.setText(folderPresenter.getName());
        hideEditor();
        inputMethodManager =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        return viewProperties;
    }

    public void setFolderPresenter(FolderPresenter folderPresenter) {
        this.folderPresenter = folderPresenter;
    }

    public FolderPresenter getFolderPresenter() {
        return folderPresenter;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        folderPresenter.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        folderPresenter.dismiss();
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
            inputMethodManager.showSoftInput(folderName,
                    InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void showToast(StateOfDocument stateOfDocument) {
        if (stateOfDocument != null) {
            final Toast toast = Toast.makeText(getActivity(),
                    stateOfDocument.toString(),
                    LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void removeDocument() {
        final RemovingFolder removing = new RemovingFolder(folderDao);
        removing.doStateWithFolder(folderPresenter);
        showToast(folderPresenter.getStateOfFolder());
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void renameDocument() {
        // handler with reactive android
        if (folderName.getText().length() == 0) {
            folderName.setText(folderPresenter.getName());
        } else {
            final String newNameFolder = folderName.getText().toString();
            folderName.setText(newNameFolder);
            final RenamingFolder renaming = new RenamingFolder(newNameFolder, folderDao);
            renaming.doStateWithFolder(folderPresenter);
            showToast(folderPresenter.getStateOfFolder());
        }
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void shareDocument() { }

    @Override
    public void addToFolderOrNewFolder(String name) { }

    @Override
    public void hideEditor() {
        folderName.setVisibleCloseButton(false);
        folderName.setEnabled(false);
        applyNameImageButton.setVisibility(View.INVISIBLE);
        cancelImageButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEditor() {
        folderName.setVisibleCloseButton(true);
        folderName.setEnabled(true);
        applyNameImageButton.setVisibility(View.VISIBLE);
        cancelImageButton.setVisibility(View.VISIBLE);
    }

    public InputMethodManager getInputMethodManager() {
        return inputMethodManager;
    }

}

