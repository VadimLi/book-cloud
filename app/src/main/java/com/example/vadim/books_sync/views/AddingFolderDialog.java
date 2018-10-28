package com.example.vadim.books_sync.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.view.WindowManager;
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
import com.example.vadim.books_sync.presenters.services.Formats;
import com.example.vadim.books_sync.presenters.states_of_folder.AddingNewFolder;
import com.example.vadim.books_sync.views.rx.ObserversForNameDocument;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

import static android.widget.Toast.LENGTH_SHORT;

public class AddingFolderDialog extends android.support.v4.app.DialogFragment
        implements DialogView, StateOwnerProperties {

    @BindView(R.id.folderName)
    CustomEditText folderName;

    @BindView(R.id.cancelImageButton)
    ImageButton cancelImageButton;

    @BindView(R.id.applyName)
    ImageButton applyName;

    @Inject
    FolderDao folderDao;

    private FolderPresenter folderPresenter;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewProperties =
                inflater.inflate(R.layout.add_new_folder_alert_dialog, null);
        ButterKnife.bind(this, viewProperties);
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        Objects.requireNonNull(getActivity())
                                .getApplication()))
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .injectAddFolderDialog(this);
        folderPresenter.attachDialog(this);
        CallbackPropertiesForAddingFolderImpl.newCallbacksForAddingFolder(this);
        validateOfNameDocument();
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
    public void showEditor() { }

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
        Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void showKeyBoard() {
        Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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

    public void setFolderPresenter(FolderPresenter folderPresenter) {
        this.folderPresenter = folderPresenter;
    }

    public FolderPresenter getFolderPresenter() {
        return folderPresenter;
    }

    @Override
    public void validateOfNameDocument() {
        final Observer nameDocumentObserver = ObserversForNameDocument
                .getNameDocumentObserver(applyName, folderName);
        CustomEditText.getPublishSubject()
                .map((Function<String, Object>) s ->
                        folderDao.findByName(s).isEmpty()
                                && Formats.notCheckNameOfFormat(s)
                                && !s.isEmpty()).subscribe(nameDocumentObserver);
    }

    @Override
    public void removeDocument() { }

    @Override
    public void renameDocument() { }

    @Override
    public void shareDocument() { }

    @Override
    public void addToFolder(final String materialName) { }

    @Override
    public void addNewFolder(String name) {
        final AddingNewFolder addingNewFolder = new AddingNewFolder(folderDao);
        addingNewFolder.doStateWithFolder(folderPresenter);
        showToast(folderPresenter.getStateOfFolder());
    }

}
