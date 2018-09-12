package com.example.vadim.books_sync.presenters;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vadim.books_sync.model.Folder;

public class FolderPresenter implements StateOwnerProperties {

    private FolderListPresenter folderListPresenter;

    private Folder folder;

    private int folderPosition;

    private StateOwnerProperties stateOwnerProperties;

    private StateOfDocument.StateOfFolder stateOfFolder;

    public void setStateOfFolder(final StateOfDocument.StateOfFolder stateOfFolder) {
        this.stateOfFolder = stateOfFolder;
    }

    public StateOfDocument.StateOfFolder getStateOfFolder() {
        return stateOfFolder;
    }

    public void attachDialog(StateOwnerProperties stateOwnerProperties) {
        this.stateOwnerProperties = stateOwnerProperties;
    }

    public StateOwnerProperties getPropertiesDialog() {
        return stateOwnerProperties;
    }

    public FolderListPresenter getFolderListPresenter() {
        return folderListPresenter;
    }

    public void setFolderListPresenter(FolderListPresenter folderListPresenter) {
        this.folderListPresenter = folderListPresenter;
    }

    public int getFolderPosition() {
        return folderPosition;
    }

    public void setFolderPosition(int folderPosition) {
        this.folderPosition = folderPosition;
    }

    public long getId() {
        return folder.getId();
    }

    public String getName() {
        return folder.getName();
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public void update(String newName) {
        folderListPresenter.updateAt(this, newName);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void removeDocument() {
        folderListPresenter.removeAt(this);
        stateOwnerProperties.removeDocument();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void renameDocument() {
        stateOwnerProperties.renameDocument();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void shareDocument() { }

    @Override
    public void addToFolderOrNewFolder(final String folderName) {
        final Folder newFolder = new Folder();
        newFolder.setName(folderName);
        this.setFolder(newFolder);
        folderListPresenter.addNewFolder(this);
        stateOwnerProperties.addToFolderOrNewFolder(folderName);
    }

    @Override
    public void dismiss() {
        stateOwnerProperties = null;
    }

}
