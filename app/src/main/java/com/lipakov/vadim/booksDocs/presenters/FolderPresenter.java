package com.lipakov.vadim.booksDocs.presenters;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.lipakov.vadim.booksDocs.model.Folder;

public class FolderPresenter implements StateOwnerProperties {

    private FolderListPresenter folderListPresenter;

    private MaterialPresenter materialPresenter;

    private Folder folder;

    private int folderPosition;

    private StateOwnerProperties stateOwnerProperties;

    private StateOfDocument.StateOfFolder stateOfFolder;

    public MaterialPresenter getMaterialPresenter() {
        return materialPresenter;
    }

    public void setMaterialPresenter(MaterialPresenter materialPresenter) {
        this.materialPresenter = materialPresenter;
    }

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
    public void addToFolder(final String materialName) {
        materialPresenter.setFolderPresenter(this);
        stateOwnerProperties.addToFolder(materialName);
    }

    @Override
    public void addNewFolder(final String folderName) {
        final Folder newFolder = new Folder();
        newFolder.setName(folderName);
        this.setFolder(newFolder);
        folderListPresenter.addNewFolder(this);
        stateOwnerProperties.addNewFolder(folderName);
    }

    @Override
    public void dismiss() {
        stateOwnerProperties = null;
    }

}
