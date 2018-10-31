package com.lipakov.vadim.booksDocs.views;

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

import com.lipakov.vadim.booksDocs.R;
import com.lipakov.vadim.booksDocs.dagger.AppModule;
import com.lipakov.vadim.booksDocs.dagger.DaggerAppComponent;
import com.lipakov.vadim.booksDocs.dagger.RoomModule;
import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.dao.MaterialFolderJoinDao;
import com.lipakov.vadim.booksDocs.presenters.FolderPresenter;
import com.lipakov.vadim.booksDocs.presenters.MaterialPresenter;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;
import com.lipakov.vadim.booksDocs.presenters.StateOwnerProperties;
import com.lipakov.vadim.booksDocs.presenters.services.Formats;
import com.lipakov.vadim.booksDocs.presenters.states_of_file.AddingFileToFolder;
import com.lipakov.vadim.booksDocs.presenters.states_of_folder.RemovingFolder;
import com.lipakov.vadim.booksDocs.presenters.states_of_folder.RenamingFolder;
import com.lipakov.vadim.booksDocs.views.rx.ObserversForNameDocument;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

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

    @Inject
    MaterialFolderJoinDao materialFolderJoinDao;

    private FolderPresenter folderPresenter;

    private InputMethodManager inputMethodManager;

    private static final String ADDING_FILE_TO_FOLDER = "Добавить в эту папку?";

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"InflateParams", "CheckResult", "ResourceAsColor"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewProperties =
                inflater.inflate(R.layout.folders_dialog_properties, null);
        final Context context = viewProperties.getContext();
        ButterKnife.bind(this, viewProperties);
        injectOfDaggerAppComponent();
        if (getContext() instanceof SelectorFolderActivity) {
            applyNameImageButton.setVisibility(View.VISIBLE);
            cancelImageButton.setVisibility(View.VISIBLE);
            renameImageButton.setVisibility(View.GONE);
            trashImageButton.setVisibility(View.GONE);
            folderName.setText(ADDING_FILE_TO_FOLDER);
            folderName.setVisibleCloseButton(false);
            folderName.setEnabled(false);
        } else {
            folderName.setText(folderPresenter.getName());
            hideEditor();
        }
        folderPresenter.attachDialog(this);
        CallbackPropertiesForFolders
                .newCallbacksEditor(this)
                .create();
        drawPropertiesDialog(viewProperties);
        validateOfNameDocument();
        inputMethodManager =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        return viewProperties;
    }

    private void injectOfDaggerAppComponent() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        getActivity().getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectDialogFragmentForFolders(this);
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
        if ( stateOfDocument instanceof StateOfDocument.StateOfFolder ||
                stateOfDocument instanceof StateOfDocument.StateOfFile ) {
            final Toast toast = Toast.makeText(getActivity(),
                    stateOfDocument.toString(),
                    LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void validateOfNameDocument() {
        final Observer nameDocumentObserver = ObserversForNameDocument
                .getNameDocumentObserver(applyNameImageButton, folderName);
        CustomEditText.getPublishSubject()
                .map((Function<String, Object>) s ->
                        folderDao.findByNameWithoutId(s,
                                folderPresenter.getId()).isEmpty()
                                && Formats.notCheckNameOfFormat(s)
                                && !s.isEmpty()).subscribe(nameDocumentObserver);
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void removeDocument() {
        final RemovingFolder removing = new RemovingFolder(folderDao);
        removing.doStateWithFolder(folderPresenter);
        showToast(folderPresenter.getStateOfFolder());
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void renameDocument() {
        final String newNameFolder = folderName.getText().toString();
        folderName.setText(newNameFolder);
        final RenamingFolder renaming = new RenamingFolder(newNameFolder, folderDao);
        renaming.doStateWithFolder(folderPresenter);
        showToast(folderPresenter.getStateOfFolder());
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void shareDocument() { }

    @Override
    public void addToFolder(String materialName) {
        final AddingFileToFolder addingFileToFolder =
                new AddingFileToFolder(materialFolderJoinDao);
        final MaterialPresenter materialPresenter = folderPresenter.getMaterialPresenter();
        addingFileToFolder.doStateWithFile(materialPresenter);
        showToast(materialPresenter.getStateOfFile());
    }

    @Override
    public void addNewFolder(String name) { }

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

}

